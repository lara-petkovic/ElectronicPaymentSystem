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

        public SubscriptionController(SubscriptionService subscriptionService, IConfiguration configuration)
        {
            this._subscriptionService = subscriptionService;
            _configuration = configuration;
        }

        [HttpPost]
        public async Task<ActionResult<Subscription>> CreateSubscription([FromBody] Subscription subscription)
        {
            var createdSubscription = await _subscriptionService.CreateSubscription(subscription);
            return Ok(createdSubscription);
        }
    }
}
