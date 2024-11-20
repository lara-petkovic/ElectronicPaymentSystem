using back_end.Dtos;
using back_end.Models;
using back_end.Services;
using Microsoft.AspNetCore.Mvc;

namespace back_end.Controllers
{
    [Route("api/psp-subscription")]
    [ApiController]
    public class PspSubscriptionController: ControllerBase
    {
        private readonly PspSubscriptionService _pspSubscriptionService;
        private readonly MerchantCredentialsService _merchantCredentialsService;

        public PspSubscriptionController(PspSubscriptionService pspSubscriptionService, MerchantCredentialsService merchantCredentialsService)
        {
            _pspSubscriptionService = pspSubscriptionService;
            _merchantCredentialsService = merchantCredentialsService;
        }

        [HttpPost("subscribe")]
        public void CreateConfiguration()
        {
            this._pspSubscriptionService.CreateSubscription();
        }

        [HttpPost("credentials")]
        public async Task<IActionResult> CreateMerchantAuthCredentials([FromBody] MerchantCredentialsDto merchantCredentialsDto)
        {
            await _merchantCredentialsService.SaveMerchantCredentialsAsync(merchantCredentialsDto.MerchantId, merchantCredentialsDto.MerchantPass);
            return Ok("Merchant credentials saved successfully");
        }

        [HttpPost("transaction")]
        public async Task<IActionResult> ProcessTransaction([FromBody] MerchantCredentials newTransactionDto)
        {
            var credentials = await _merchantCredentialsService.GetMerchantCredentialsAsync();

            if (credentials == null)
            {
                return BadRequest("Merchant credentials not found");
            }

            newTransactionDto.MerchantId = credentials.MerchantId;
            newTransactionDto.MerchantPass = credentials.MerchantPass;

            await _pspSubscriptionService.ProcessTransactionAsync(newTransactionDto);

            return Ok("Transaction processed successfully");
        }
    }
}
