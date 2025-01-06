using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using System.Text;

namespace Explorer.API.Startup;

public static class AuthConfiguration
{
    public static IServiceCollection ConfigureAuth(this IServiceCollection services)
    {
        ConfigureAuthentication(services);
        ConfigureAuthorizationPolicies(services);
        return services;
    }

    private static void ConfigureAuthentication(IServiceCollection services)
    {
        var key = Environment.GetEnvironmentVariable("JWT_KEY") ?? "1n29dmxBY9O2nA93KDmNO7zA1P3dmnC7";
        var issuer = Environment.GetEnvironmentVariable("JWT_ISSUER") ?? "web_shop";
        var audience = Environment.GetEnvironmentVariable("JWT_AUDIENCE") ?? "web_shop-front.com";

        services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
            .AddJwtBearer(options =>
            {
                options.TokenValidationParameters = new TokenValidationParameters
                {
                    ValidateIssuer = true,
                    ValidateAudience = true,
                    ValidateIssuerSigningKey = true,
                    ValidateLifetime = true,
                    ClockSkew = TimeSpan.Zero,
                    ValidIssuer = issuer,
                    ValidAudience = audience,
                    IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(key))
                };
            });
    }


    private static void ConfigureAuthorizationPolicies(IServiceCollection services)
    {
        services.AddAuthorization(options =>
        {
            options.AddPolicy("UserPolicy", policy => policy.RequireRole("user"));
            options.AddPolicy("AdminPolicy", policy => policy.RequireRole("admin"));
        });
    }
}