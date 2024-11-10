using back_end.Services;
using Microsoft.AspNetCore.Mvc;

namespace back_end.Controllers
{
    [Route("api/psp-subscription")]
    [ApiController]
    public class PspSubscriptionController: ControllerBase
    {
        private readonly PspSubscriptionService _pspSubscriptionService;
        private readonly IConfiguration _configuration;

        public PspSubscriptionController(PspSubscriptionService pspSubscriptionService, IConfiguration configuration)
        {
            _pspSubscriptionService = pspSubscriptionService;
            _configuration = configuration;
        }
        [HttpPost]
        public void CreateConfiguration()
        {
            this._pspSubscriptionService.CreateSubscription();
        }
    }
}
