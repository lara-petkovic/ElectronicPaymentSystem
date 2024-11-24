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

        public async Task<Models.Transaction> SetStatusTransaction(Models.Transaction transaction, String status)
        {
            Models.Transaction tr = GetTransaction(transaction.Id).Result;
            tr.Status = status;
            _context.Transactions.Update(tr);
            await _context.SaveChangesAsync();
            return transaction;
        }

        public async Task<Models.Transaction> GetTransaction(long merchantOrderId)
        {
            return _context.Transactions.Where(p => p.Id == merchantOrderId).FirstOrDefault();
        }

        public async Task<List<Models.Transaction>> GetSuccessfulTransactions(long userId)
        {
            return _context.Transactions.Where(p => p.Status == "success" && p.UserId == userId).ToList();
        }
    }
}
