package codeit.gatcha.api.security.service;

import codeit.gatcha.api.security.entity.RefreshToken;
import codeit.gatcha.api.security.repo.RefreshTokenRepository;
import codeit.gatcha.domain.user.entity.GatchaUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService
{
    private final RefreshTokenRepository refreshTokenRepository;

    public boolean refreshTokenExists(String refreshToken)
    {
        return refreshTokenRepository.findByValue(refreshToken).isPresent();
    }

    public void saveNewRefreshToken(String refreshTokenString, GatchaUser user)
    {
        RefreshToken refreshToken = RefreshToken
                .builder()
                .value(refreshTokenString)
                .userId(user.getId())
                .creationDate(LocalDateTime.now())
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void deleteByValue(String refreshToken)
    {
        refreshTokenRepository.deleteByValue(refreshToken);
    }
}
