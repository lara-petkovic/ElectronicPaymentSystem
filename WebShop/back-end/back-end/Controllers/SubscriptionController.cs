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

        public SubscriptionController(SubscriptionService subscriptionService, IConfiguration configuration)
        {
            this._subscriptionService = subscriptionService;
            _configuration = configuration;
        }

        [HttpPost]
        public async Task<ActionResult<Subscription>> CreateSubscription([FromBody] Subscription subscription)
        {
            var createdSubscription = await _subscriptionService.CreateSubscription(subscription);

            Transaction newTransaction = new Transaction();
            newTransaction.PurcasedServiceId = null;
            newTransaction.PurchasedPackageId = subscription.PackageId;
            newTransaction.UserId = subscription.UserId;
            newTransaction.Status = "CREATED";
            newTransaction.Timestamp = DateTime.UtcNow;
            Package p = _packageService.Get(subscription.PackageId);
            newTransaction.Amount = (double)p.Price;
            var savedTransaction = await _transactionService.SaveTransaction(newTransaction);
            await _pspSubscriptionService.ProcessTransactionAsync(savedTransaction);
            return Ok(createdSubscription);
        }
    }
}
