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
        [HttpPost("subscribe")]
        public void CreateConfiguration()
        {
            this._pspSubscriptionService.CreateSubscription();
        }
        [HttpPost("credentials")]
        public void CreateMerchantAuthCredentials()
        {
            //sacuvati merchant id i pass i slati uz svaku transakciju
            Console.WriteLine("AAA");
        }
    }
}
