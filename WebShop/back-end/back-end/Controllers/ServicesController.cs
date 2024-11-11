using back_end.Models;
using back_end.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace back_end.Controllers
{
    [Route("api/services")]
    [ApiController]
    public class ServicesController : ControllerBase
    {
        private ServiceService _serviceService;

        public ServicesController(ServiceService serviceService)
        {
            _serviceService = serviceService;
        }

        [Authorize]
        [HttpGet]
        public async Task<ActionResult<List<Service>>> GetServices()
        {
            var services = await _serviceService.GetServicesAsync();
            return Ok(services);
        }
    }
}
