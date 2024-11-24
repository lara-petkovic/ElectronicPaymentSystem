using back_end.Models;

namespace back_end.Dtos
{
    public class ItemDto
    {
        public string Name { get; set; }
        public string? Description { get; set; }
        public ServiceType? Type { get; set; }
        public decimal Price { get; set; }
        public bool? IsBusinessPackage { get; set; }
        public bool IsPackage { get; set; }
        public string Status { get; set; }
        public string? StartDate { get; set; }

        public ItemDto(Package package, string status, DateTime startDate) {
            Name = package.Name;
            Description = null;
            Type = null;
            Price = package.Price;
            IsBusinessPackage = package.IsBusinessPackage;
            IsPackage = true;
            Status = status;
            StartDate = startDate.ToString();
        }

        public ItemDto(Service service)
        {
            Name = service.Name;
            Description = service.Description;
            Type = service.Type;
            Price = service.Price;
            IsBusinessPackage = null;
            IsPackage = false;
            Status = "CREATED";
        }
    }
}
