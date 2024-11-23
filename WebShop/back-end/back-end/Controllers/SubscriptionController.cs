using back_end.Models;
using back_end.Services;
using Microsoft.AspNetCore.Mvc;

namespace back_end.Controllers
{
    [Route("api/subscriptions")]
    [ApiController]
    public class SubscriptionController : ControllerBase
    {
        private readonly SubscriptionService _subscriptionService;
        private readonly IConfiguration _configuration;
        private readonly TransactionService _transactionService;
        private readonly PspSubscriptionService _pspSubscriptionService;
        private readonly Services.PackageService _packageService;

        public SubscriptionController(SubscriptionService subscriptionService, IConfiguration configuration, Services.PackageService packageService, TransactionService transactionService, PspSubscriptionService pspSubscriptionService)
        {
            _subscriptionService = subscriptionService;
            _configuration = configuration;
            _packageService = packageService;
            _transactionService = transactionService;
            _pspSubscriptionService = pspSubscriptionService;
        }

        [HttpPost]
        public async Task<ActionResult<Subscription>> CreateSubscription([FromBody] Subscription subscription)
        {
            if (subscription == null || subscription.PackageId == null || subscription.UserId == null)
            {
                return BadRequest("Invalid subscription data.");
            }

            var createdSubscription = await _subscriptionService.CreateSubscription(subscription);

            var package = _packageService.Get(subscription.PackageId);
            if (package == null)
            {
                return NotFound($"Package with ID {subscription.PackageId} not found.");
            }

            var newTransaction = new Transaction
            {
                PurchasedPackageId = subscription.PackageId,
                UserId = subscription.UserId,
                Status = "CREATED",
                Timestamp = DateTime.UtcNow,
                Amount = (double)package.Price
            };

            var savedTransaction = await _transactionService.SaveTransaction(newTransaction);
            await _pspSubscriptionService.ProcessTransactionAsync(savedTransaction);

            return Ok(createdSubscription);
        }
    }
}
