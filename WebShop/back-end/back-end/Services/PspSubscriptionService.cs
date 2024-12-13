using System.Text;
using back_end.Models;
using back_end.Dtos;
using System.Text.Json;

namespace back_end.Services
{
    public class PspSubscriptionService
    {
        private static readonly HttpClient httpClient = new HttpClient();
        private const string BaseUrl = "http://localhost:8086/api";

        private static readonly string SubscriptionEndpoint = $"{BaseUrl}/subscription";
        private static readonly string PaymentOptionEndpoint = $"{BaseUrl}/subscription/5275";
        private static readonly string TransactionEndpoint = $"{BaseUrl}/transaction";

        public async void CreateSubscription() {
            var jsonData = "{\"apiKey\": \"5275\"}";
            var content = new StringContent(jsonData, Encoding.UTF8, "application/json");

            try
            {
                HttpResponseMessage response = await httpClient.PostAsync(SubscriptionEndpoint, content);
                response.EnsureSuccessStatusCode(); 

                string responseData = await response.Content.ReadAsStringAsync();
                Console.WriteLine("Server response: " + responseData);
            }
            catch (HttpRequestException e)
            {
                Console.WriteLine("An error has occured: " + e.Message);
            }
        }

        public async Task<List<PaymentOptionDto>> GetPaymentOptions()
        {
            try
            {
                HttpResponseMessage response = await httpClient.GetAsync(PaymentOptionEndpoint);
                response.EnsureSuccessStatusCode();

                string responseData = await response.Content.ReadAsStringAsync();

                var paymentOptions = JsonSerializer.Deserialize<List<PaymentOptionDto>>(responseData, new JsonSerializerOptions
                {
                    PropertyNameCaseInsensitive = true
                });

                return paymentOptions ?? new List<PaymentOptionDto>();
            }
            catch (HttpRequestException e)
            {
                Console.WriteLine("An error has occurred while fetching payment options: " + e.Message);
                return new List<PaymentOptionDto>();
            }
        }

        public async Task<bool> RemovePaymentOption(PaymentOptionDto option)
        {
            try
            {
                var jsonData = JsonSerializer.Serialize(option);
                var content = new StringContent(jsonData, Encoding.UTF8, "application/json");

                var request = new HttpRequestMessage
                {
                    Method = HttpMethod.Put,
                    RequestUri = new Uri(PaymentOptionEndpoint),
                    Content = content
                };

                HttpResponseMessage response = await httpClient.SendAsync(request);

                response.EnsureSuccessStatusCode();
                return true;
            }
            catch (HttpRequestException e)
            {
                Console.WriteLine("An error occurred while removing the payment option: " + e.Message);
                return false;
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

            try
            {
                httpClient.DefaultRequestHeaders.Add("Port", "5275");

                HttpResponseMessage response = await httpClient.PostAsync(TransactionEndpoint, jsonData);
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
