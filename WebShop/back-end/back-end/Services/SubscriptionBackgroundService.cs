namespace back_end.Services
{
    public class SubscriptionBackgroundService : BackgroundService
    {
        private readonly IServiceProvider _serviceProvider;

        public SubscriptionBackgroundService(IServiceProvider serviceProvider)
        {
            _serviceProvider = serviceProvider;
        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            while (!stoppingToken.IsCancellationRequested)
            {
                DateTime nextRun = GetNextRunTime();
                TimeSpan delay = nextRun - DateTime.UtcNow;

                if (delay > TimeSpan.Zero)
                {
                    await Task.Delay(delay, stoppingToken);
                }

                try
                {
                    using (var scope = _serviceProvider.CreateScope())
                    {
                        var subscriptionService = scope.ServiceProvider.GetRequiredService<SubscriptionService>();
                        await subscriptionService.ProcessSubscriptionTransactions();
                    }
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"Error processing subscriptions: {ex.Message}");
                }
            }
        }

        private DateTime GetNextRunTime()
        {
            DateTime now = DateTime.UtcNow;
            return new DateTime(now.Year, now.Month, 1).AddMonths(1);
        }
    }
}
