using back_end.Models;
using Microsoft.EntityFrameworkCore;

namespace back_end.Data
{
    public class AppDbContext : DbContext
    {
        protected readonly IConfiguration Configuration;

        public AppDbContext(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder.UseNpgsql(Configuration.GetConnectionString("WebApiDatabase"));
        }

        public DbSet<User> Users { get; set; }
        public DbSet<Service> Services { get; set; }
        public DbSet<Package> Packages { get; set; }
        public DbSet<Subscription> Subscriptions { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            modelBuilder.Entity<PackageService>()
                .HasKey(ps => new { ps.PackageId, ps.ServiceId });

            modelBuilder.Entity<PackageService>()
                .HasOne(ps => ps.Package)
                .WithMany(p => p.PackageServices)
                .HasForeignKey(ps => ps.PackageId);

            modelBuilder.Entity<PackageService>()
                .HasOne(ps => ps.Service)
                .WithMany(s => s.PackageServices)
                .HasForeignKey(ps => ps.ServiceId);

            modelBuilder.Entity<User>()
                .HasMany(u => u.Subscriptions)
                .WithOne(s => s.User)
                .HasForeignKey(s => s.UserId);

            modelBuilder.Entity<Subscription>()
                .HasOne(s => s.Package)
                .WithMany()
                .HasForeignKey(s => s.PackageId);

            SeedData(modelBuilder);
        }

        private static void SeedData(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<User>().HasData(
                new User { Id = -1, Username = "larapetkovic", FirstName = "Lara", LastName = "Petkovic", Password = "password", Email = "lara@gmail.com" },
                new User { Id = -2, Username = "jelisavetaletic", FirstName = "Jelisaveta", LastName = "Letic", Password = "password", Email = "jelly@hotmail.com" },
                new User { Id = -3, Username = "aaa", FirstName = "aaa", LastName = "aaa", Password = "aaa", Email = "aaa@aaa.com" }
            );

            modelBuilder.Entity<Service>().HasData(
                new Service { Id = -1, Name = "Mobile Plan", Description = "Mobile Service Plan", Type = ServiceType.Mobile, Price = 20 },
                new Service { Id = -2, Name = "Internet Plan", Description = "Home Internet Plan", Type = ServiceType.Internet, Price = 30 },
                new Service { Id = -3, Name = "Digital TV Plan", Description = "Digital TV Service Plan", Type = ServiceType.DigitalTV, Price = 25 }
            );

            modelBuilder.Entity<Package>().HasData(
                new Package { Id = -1, Name = "Basic Package", Price = 50, IsBusinessPackage = false },
                new Package { Id = -2, Name = "Business Package", Price = 100, IsBusinessPackage = true }
            );

            modelBuilder.Entity<Subscription>().HasData(
               new Subscription
               {
                   Id = -1,
                   UserId = -1,
                   PackageId = -1,
                   StartDate = DateTime.SpecifyKind(new DateTime(2024, 1, 1, 0, 0, 0), DateTimeKind.Utc),
                   DurationInYears = 1
               }
           );

            modelBuilder.Entity<PackageService>().HasData(
                new PackageService { PackageId = -1, ServiceId = -1 }, // Basic Package includes Mobile Plan
                new PackageService { PackageId = -1, ServiceId = -2 }, // Basic Package includes Internet Plan
                new PackageService { PackageId = -2, ServiceId = -1 }, // Business Package includes Mobile Plan
                new PackageService { PackageId = -2, ServiceId = -2 }, // Business Package includes Internet Plan
                new PackageService { PackageId = -2, ServiceId = -3 }  // Business Package includes Digital TV Plan
            );
        }
    }
}
