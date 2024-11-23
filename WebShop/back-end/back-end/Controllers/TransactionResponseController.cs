using back_end.Dtos;
using Microsoft.AspNetCore.Mvc;

namespace back_end.Controllers
{
    [Route("api/response")]
    [ApiController]
    public class TransactionResponseController : ControllerBase
    {
        public TransactionResponseController() { }

        [HttpPost]
        public ActionResult TransactionResponse([FromBody] MerchantOrderIdDto merchantOrderIdDto)
        {
            //Sacuvaj orderid
            //javi korisniku da je proslo
            //TODO
            return Ok();
        }
    }
}
