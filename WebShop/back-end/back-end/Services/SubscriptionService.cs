using back_end.Data;
using back_end.Models;
using System.Net.Http;
using System.Text;

namespace back_end.Services
{
    public class SubscriptionService
    {
        private readonly AppDbContext _context;
        private readonly PackageService _packageService;
        private static readonly HttpClient httpClient = new HttpClient();
        public SubscriptionService(AppDbContext context, PackageService packageService)
        {
            _context = context;
            _packageService = packageService;
        }
        public async Task<Subscription> CreateSubscription(Subscription subscription)
        {
            if (subscription == null)
            {
                throw new ArgumentNullException(nameof(subscription));
            }

            var user = await _context.Users.FindAsync(subscription.UserId);
            if (user == null)
            {
                throw new InvalidOperationException("User not found");
            }

            subscription.StartDate = DateTime.UtcNow;

            _context.Subscriptions.Add(subscription);
            await _context.SaveChangesAsync();

            return subscription;
        }

        public async Task ProcessSubscriptionTransactions()
        {
            DateTime currentDate = DateTime.UtcNow;

            var subscriptions = _context.Subscriptions
                .Where(sub => sub.StartDate.AddYears(sub.DurationInYears) > currentDate)
                .ToList();

            foreach (var subscription in subscriptions)
            {
                if (subscription.StartDate.AddMonths(1) <= currentDate)
                {
                    Package package = _packageService.Get(subscription.PackageId);
                    var transaction = new Transaction
                    {
                        Amount = (double)package.Price,
                        Timestamp = DateTime.UtcNow,
                        UserId = subscription.UserId,
                        PurcasedServiceId = null,
                        PurchasedPackageId = package.Id
                    };

                    var jsonData = new StringContent(
                    $"{{\"port\":\"5275\", " +
                    $"\"amount\": {transaction.Amount}, " +
                    $"\"merchantOrderId\": {transaction.Id}, " +
                    $"\"merchantTimestamp\": \"{transaction.Timestamp}\"}}",
                    Encoding.UTF8, "application/json");


                    string url = "http://localhost:8086/api/transaction";
                    try
                    {
                        HttpResponseMessage response = await httpClient.PostAsync(url, jsonData);
                        response.EnsureSuccessStatusCode();

                        string responseData = await response.Content.ReadAsStringAsync();
                        Console.WriteLine("Server response: " + responseData);
                    }
                    catch (HttpRequestException e)
                    {
                        Console.WriteLine("An error has occurred: " + e.Message);
                    }

                    _context.Transactions.Add(transaction);
                }
            }

            await _context.SaveChangesAsync();
        }

    }
}
