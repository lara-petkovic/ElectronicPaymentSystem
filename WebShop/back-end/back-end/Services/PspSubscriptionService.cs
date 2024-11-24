using System.Text;
using System;
using back_end.Models;
using back_end.Data;
using Microsoft.EntityFrameworkCore;

namespace back_end.Services
{
    public class PspSubscriptionService
    {
        private static readonly HttpClient httpClient = new HttpClient();

        public async void CreateSubscription() {
            var jsonData = "{\"apiKey\": \"5275\"}";
            var content = new StringContent(jsonData, Encoding.UTF8, "application/json");
            string url = "http://localhost:8086/api/subscription";
            try
            {
                HttpResponseMessage response = await httpClient.PostAsync(url, content);
                response.EnsureSuccessStatusCode(); 

                string responseData = await response.Content.ReadAsStringAsync();
                Console.WriteLine("Server response: " + responseData);
            }
            catch (HttpRequestException e)
            {
                Console.WriteLine("An error has occured: " + e.Message);
            }
        }

        public async Task ProcessTransactionAsync(Transaction transaction)
        {
            var jsonData = new StringContent(
                $"{{"+
                $"\"amount\": {transaction.Amount}, " +
                $"\"merchantOrderId\": {transaction.Id}, " +
                $"\"merchantTimestamp\": \"{transaction.Timestamp}\"}}",
                Encoding.UTF8, "application/json");


            string url = "http://localhost:8086/api/transaction";
            try
            {
                httpClient.DefaultRequestHeaders.Add("Port", "5275");

                HttpResponseMessage response = await httpClient.PostAsync(url, jsonData);
                response.EnsureSuccessStatusCode();

                string responseData = await response.Content.ReadAsStringAsync();
                Console.WriteLine("Server response: " + responseData);
            }
            catch (HttpRequestException e)
            {
                Console.WriteLine("An error has occurred: " + e.Message);
            }
        }
    }
}
