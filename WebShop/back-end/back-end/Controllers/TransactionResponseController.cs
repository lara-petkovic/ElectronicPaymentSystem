using back_end.Dtos;
using back_end.Models;
using back_end.Services;
using Microsoft.AspNetCore.Mvc;

namespace back_end.Controllers
{
    [Route("api/response")]
    [ApiController]
    public class TransactionResponseController : ControllerBase
    {
        private TransactionService _transactionService;
        private Services.PackageService _packageService;
        private ServiceService _serviceService;
        public TransactionResponseController(TransactionService transactionService, Services.PackageService packageService, ServiceService serviceService) {
            _transactionService = transactionService;
            _packageService = packageService;
            _serviceService = serviceService;
        }

        [HttpPost]
        public ActionResult TransactionResponse([FromBody] MerchantOrderIdDto merchantOrderIdDto)
        {
            Transaction transaction = _transactionService.GetTransaction(merchantOrderIdDto.MerchantOrderId).Result;
            _ = _transactionService.SetStatusTransaction(transaction, merchantOrderIdDto.Status);
            return Ok();
        }

        [HttpGet("{loggedUserId}")]
        public ActionResult<List<ItemDto>> GetSuccessfulTransactions(int loggedUserId)
        {
            var successfulTransactions = _transactionService.GetSuccessfulTransactions(loggedUserId);
            List<ItemDto> result = new List<ItemDto>();

            foreach (var transaction in successfulTransactions.Result)
            {   
                if(transaction.PurcasedServiceId == null)
                {
                    Package package = _packageService.Get((int)transaction.PurchasedPackageId);
                    ItemDto item = new ItemDto(package);
                    result.Add(item);
                }

                else
                {
                    Service service = _serviceService.Get((int)transaction.PurcasedServiceId);
                    ItemDto item = new ItemDto(service);
                    result.Add(item);
                }
                
            }
            return Ok(result);
        }
    }
}
