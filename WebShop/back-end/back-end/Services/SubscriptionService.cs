using back_end.Data;
using back_end.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
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

        public Subscription FindUsersSubscriptionByPackage(int userId, Package package)
        {
            return _context.Subscriptions.Where(s => s.UserId == userId && s.PackageId == package.Id).FirstOrDefault();
        }

        public async Task<ActionResult> CancelSubscription(Subscription subscription)
        {
            var existingSubscription = await _context.Subscriptions
                .FirstOrDefaultAsync(s => s.UserId == subscription.UserId && s.PackageId == subscription.PackageId);

            if (existingSubscription == null)
            {
                return new NotFoundObjectResult("Subscription not found.");
            }

            existingSubscription.Status = "CANCELED";
            _context.Subscriptions.Update(existingSubscription);

            var result = await _context.SaveChangesAsync();

            if (result > 0)
            {
                return new OkObjectResult("Subscription successfully canceled.");
            }
            else
            {
                return new BadRequestObjectResult("Error occurred while canceling the subscription.");
            }
        }

        public async Task<ActionResult> ExtendSubscription(Subscription subscription, int years)
        {
            var existingSubscription = await _context.Subscriptions
                .FirstOrDefaultAsync(s => s.UserId == subscription.UserId && s.PackageId == subscription.PackageId);

            if (existingSubscription == null)
            {
                return new NotFoundObjectResult("Subscription not found.");
            }

            existingSubscription.StartDate = existingSubscription.StartDate.AddYears(years);
            _context.Subscriptions.Update(existingSubscription);

            var result = await _context.SaveChangesAsync();

            if (result > 0)
            {
                return new OkObjectResult("Subscription successfully extended.");
            }
            else
            {
                return new BadRequestObjectResult("Error occurred while extending the subscription.");
            }
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
            subscription.Status = "CREATED";

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
                        $"{{" +
                        $"\"amount\": {transaction.Amount}, " +
                        $"\"merchantOrderId\": {transaction.Id}, " +
                        $"\"merchantTimestamp\": \"{transaction.Timestamp}\"}}",
                        Encoding.UTF8, "application/json");


                    string url = "https://localhost:8086/api/transaction";
                    try
                    {
                        httpClient.DefaultRequestHeaders.Add("Port", "5275");

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
