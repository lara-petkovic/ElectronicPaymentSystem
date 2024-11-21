using back_end.Data;
using back_end.Models;
using Microsoft.EntityFrameworkCore;
using System.Transactions;

namespace back_end.Services
{
    public class TransactionService
    {
        private readonly AppDbContext _context;

        public TransactionService(AppDbContext context)
        {
            _context = context;
        }

        public async Task<Models.Transaction> SaveTransaction(Models.Transaction transaction)
        {
            _context.Transactions.Add(transaction);
            await _context.SaveChangesAsync();
            return transaction;
        }
    }
}
