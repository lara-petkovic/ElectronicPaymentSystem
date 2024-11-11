namespace back_end.Models
{
    public class Service
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public ServiceType Type { get; set; }
        public decimal Price { get; set; }
        public ICollection<PackageService> PackageServices { get; set; }
    }
}
