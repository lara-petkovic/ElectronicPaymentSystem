namespace back_end.Models
{
    public class Transaction
    {
        public int Id { get; set; }
        public double Amount { get; set; }
        public DateTime Timestamp { get; set; }
        public int UserId { get; set; }
        public string Status {  get; set; }
        public int? PurcasedServiceId { get; set; }
        public int? PurchasedPackageId { get; set; }
    }
}
