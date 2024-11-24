namespace back_end.Models
{
    public class Subscription
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public int PackageId { get; set; }
        public DateTime StartDate { get; set; } = DateTime.UtcNow;
        public int DurationInYears { get; set; }
        public string Status { get; set; }

        public User? User { get; set; }
        public Package? Package { get; set; }
    }
}
