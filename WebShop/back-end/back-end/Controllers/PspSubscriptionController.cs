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
        private readonly TransactionService _transactionService;

        public PspSubscriptionController(PspSubscriptionService pspSubscriptionService, MerchantCredentialsService merchantCredentialsService, TransactionService transactionService)
        {
            _pspSubscriptionService = pspSubscriptionService;
            _merchantCredentialsService = merchantCredentialsService;
            _transactionService = transactionService;
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
        public async Task<IActionResult> ProcessTransaction([FromBody] Transaction newTransaction)
        {
            var credentials = await _merchantCredentialsService.GetMerchantCredentialsAsync();

            if (credentials == null)
            {
                return BadRequest("Merchant credentials not found");
            }

            var savedTransaction = await _transactionService.SaveTransaction(newTransaction);
            await _pspSubscriptionService.ProcessTransactionAsync(savedTransaction, credentials);

            return Ok("Transaction saved and processed successfully");
        }
    }
}
