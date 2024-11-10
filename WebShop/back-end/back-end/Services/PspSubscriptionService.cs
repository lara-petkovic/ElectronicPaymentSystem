using System.Text;
using System;

namespace back_end.Services
{
    public class PspSubscriptionService
    {
        private static readonly HttpClient httpClient = new HttpClient();

        public async void CreateSubscription() {
            var jsonData = "{\"apiKey\":\"7098\"}";
            var content = new StringContent(jsonData, Encoding.UTF8, "application/json");
            string url = "http://localhost:8086";
            try
            {
                HttpResponseMessage response = await httpClient.PostAsync(url, content);
                response.EnsureSuccessStatusCode(); 

                string responseData = await response.Content.ReadAsStringAsync();
                Console.WriteLine("Odgovor sa servera: " + responseData);
            }
            catch (HttpRequestException e)
            {
                Console.WriteLine("Došlo je do greške: " + e.Message);
            }
        }
    }
}
