namespace back_end.Models
{
    public class Package
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public decimal Price { get; set; }
        public bool IsBusinessPackage { get; set; }

        public ICollection<Service> Services { get; set; }
    }

}
