using back_end.Data;
using back_end.Models;
using Microsoft.EntityFrameworkCore;

namespace back_end.Services
{
    public class MerchantCredentialsService
    {

        private readonly AppDbContext _context;

        public MerchantCredentialsService(AppDbContext context)
        {
            _context = context;
        }

        public async Task SaveMerchantCredentialsAsync(string merchantId, string merchantPass)
        {
            var credentials = new MerchantCredentials
            {
                MerchantId = merchantId,
                MerchantPass = merchantPass
            };

            _context.MerchantCredentials.Add(credentials);
            await _context.SaveChangesAsync();
        }

        public async Task<MerchantCredentials> GetMerchantCredentialsAsync()
        {
            return await _context.MerchantCredentials.FirstOrDefaultAsync();
        }
    }
}