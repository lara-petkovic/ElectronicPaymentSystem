namespace back_end.Models
{
    public class PackageService
    {
        public int PackageId { get; set; }
        public Package Package { get; set; }

        public int ServiceId { get; set; }
        public Service Service { get; set; }
    }
}
