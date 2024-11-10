using back_end.Models;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace back_end.Services
{
    public class AuthenticationTokensDto
    {
        public long Id { get; set; }
        public string AccessToken { get; set; }
    }
    public class TokenService
    {
        private readonly IConfiguration _configuration;

        private readonly string _key = Environment.GetEnvironmentVariable("JWT_KEY") ?? "1n29dmxBY9O2nA93KDmNO7zA1P3dmnC7";
        private readonly string _issuer = Environment.GetEnvironmentVariable("JWT_ISSUER") ?? "web_shop";
        private readonly string _audience = Environment.GetEnvironmentVariable("JWT_AUDIENCE") ?? "web_shop-front.com";

        public TokenService(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        public AuthenticationTokensDto GenerateToken(User user, long personId)
        {
            var authenticationResponse = new AuthenticationTokensDto();

            var claims = new List<Claim>
        {
            new(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()),
            new("id", user.Id.ToString()),
            new("username", user.Username),
            new("personId", personId.ToString())
        };

            var jwt = CreateToken(claims, 60 * 24);
            authenticationResponse.Id = user.Id;
            authenticationResponse.AccessToken = jwt;

            return authenticationResponse;
        }

        private string CreateToken(IEnumerable<Claim> claims, double expirationTimeInMinutes)
        {
            var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_key));
            var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);

            var token = new JwtSecurityToken(
                _issuer,
                _audience,
                claims,
                expires: DateTime.Now.AddMinutes(expirationTimeInMinutes),
                signingCredentials: credentials);

            return new JwtSecurityTokenHandler().WriteToken(token);
        }
    }
}
