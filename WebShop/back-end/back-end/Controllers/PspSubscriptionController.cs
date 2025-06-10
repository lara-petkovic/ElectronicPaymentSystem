using back_end.Dtos;
using back_end.Models;
using back_end.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace back_end.Controllers
{
    [Route("api/psp-subscription")]
    [ApiController]
    public class PspSubscriptionController: ControllerBase
    {
        private readonly PspSubscriptionService _pspSubscriptionService;
        private readonly TransactionService _transactionService;

        public PspSubscriptionController(PspSubscriptionService pspSubscriptionService, TransactionService transactionService)
        {
            _pspSubscriptionService = pspSubscriptionService;
            _transactionService = transactionService;
        }

        //[Authorize(Policy = "AdminPolicy")]
        [HttpPost("subscribe")]
        public void CreateConfiguration()
        {
            this._pspSubscriptionService.CreateSubscription();
        }

        [HttpPost("transaction")]
        public async Task<IActionResult> ProcessTransaction([FromBody] Transaction newTransaction)
        {
            var savedTransaction = await _transactionService.SaveTransaction(newTransaction);
            await _pspSubscriptionService.ProcessTransactionAsync(savedTransaction);

            return Ok("Transaction saved and processed successfully");
        }

        //[Authorize(Policy = "AdminPolicy")]
        [HttpGet("payment-options")]
        public async Task<IActionResult> GetPaymentOptions()
        {
            var paymentOptions = await _pspSubscriptionService.GetPaymentOptions();
            return Ok(paymentOptions);
        }

        //[Authorize(Policy = "AdminPolicy")]
        [HttpPut("payment-option")]
        public async Task<IActionResult> RemovePaymentOption([FromBody] PaymentOptionDto option)
        {
            var success = await _pspSubscriptionService.RemovePaymentOption(option);
            if (success)
            {
                return Ok("Payment option removed successfully");
            }
            else
            {
                return BadRequest("Failed to remove payment option");
            }
        }
    }
}
