using back_end.Dtos;
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
        private readonly ServiceService _serviceService;

        public SubscriptionController(SubscriptionService subscriptionService, IConfiguration configuration, Services.PackageService packageService, TransactionService transactionService, PspSubscriptionService pspSubscriptionService, ServiceService serviceService)
        {
            _subscriptionService = subscriptionService;
            _configuration = configuration;
            _packageService = packageService;
            _transactionService = transactionService;
            _pspSubscriptionService = pspSubscriptionService;
            _serviceService = serviceService;
        }

        [HttpPut("cancel/{userId}/{itemName}/{itemPrice}")]
        public async Task<ActionResult> CancelSubscription(int userId, string itemName, double itemPrice)
        {
            var decodedItemName = Uri.UnescapeDataString(itemName);
            var package = _packageService.GetPackageByNameAndPrice(decodedItemName, itemPrice);
            var subscription = _subscriptionService.FindUsersSubscriptionByPackage(userId, package);

            if (subscription == null)
            {
                return NotFound("Subscription not found.");
            }

            var result = await _subscriptionService.CancelSubscription(subscription);
            return result;
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
