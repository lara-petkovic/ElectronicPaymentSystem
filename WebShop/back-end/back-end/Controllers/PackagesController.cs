using back_end.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace back_end.Controllers
{
    [Route("api/packages")]
    [ApiController]
    public class PackagesController : ControllerBase
    {
        private readonly Services.PackageService _packageService;

        public PackagesController(Services.PackageService packageService)
        {
            _packageService = packageService;
        }

        [Authorize]
        [HttpGet]
        public async Task<ActionResult<List<Package>>> GetPackages()
        {
            var packages = await _packageService.GetPackagesAsync();
            return Ok(packages);
        }
    }
}
