using back_end.Data;
using back_end.Models;

namespace back_end.Services
{
    public class SubscriptionService
    {
        private readonly AppDbContext _context;
        public SubscriptionService(AppDbContext context)
        {
            _context = context;
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
    }
}
