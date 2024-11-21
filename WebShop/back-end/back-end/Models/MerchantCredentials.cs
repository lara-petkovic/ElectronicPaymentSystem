namespace back_end.Models
{
    public class MerchantCredentials
    {
        public int Id { get; set; }
        public string MerchantId { get; set; }
        public string MerchantPass { get; set; }
        public MerchantCredentials() { }
    }
}
