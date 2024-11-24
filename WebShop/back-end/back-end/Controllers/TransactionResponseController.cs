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
        private PspSubscriptionService _pspSubscriptionService;
        private SubscriptionService _subscriptionService;
        public TransactionResponseController(TransactionService transactionService, Services.PackageService packageService, ServiceService serviceService, PspSubscriptionService pspSubscriptionService, SubscriptionService subscriptionService)
        {
            _transactionService = transactionService;
            _packageService = packageService;
            _serviceService = serviceService;
            _pspSubscriptionService = pspSubscriptionService;
            _subscriptionService = subscriptionService;
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
                    var subscription = _subscriptionService.FindUsersSubscriptionByPackage(loggedUserId, package);

                    if(subscription != null)
                    {
                        ItemDto item = new ItemDto(package, subscription.Status, subscription.StartDate);
                        result.Add(item);
                    }
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

        [HttpPost("service-transaction/{serviceId}/{userId}")]
        public async Task<ActionResult<Subscription>> CreateTransaction(int serviceId, int userId)
        {
            var service = _serviceService.Get(serviceId);

            var newTransaction = new Transaction
            {
                PurcasedServiceId = service.Id,
                UserId = userId,
                Status = "CREATED",
                Timestamp = DateTime.UtcNow,
                Amount = (double)service.Price
            };

            var savedTransaction = await _transactionService.SaveTransaction(newTransaction);
            await _pspSubscriptionService.ProcessTransactionAsync(savedTransaction);

            return Ok(savedTransaction);
        }
    }
}
