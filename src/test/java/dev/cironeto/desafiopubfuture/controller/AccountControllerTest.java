package dev.cironeto.desafiopubfuture.controller;

import dev.cironeto.desafiopubfuture.dto.AccountDto;
import dev.cironeto.desafiopubfuture.service.AccountService;
import dev.cironeto.desafiopubfuture.util.AccountDtoCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Objects;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Account Controller")
class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountServiceMock;

    private Pageable pageable;
    private AccountDto accountDto;
    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    @BeforeEach
    void setUp() {
        accountDto = AccountDtoCreator.createAccountDto();
        PageImpl<AccountDto> accountPage = new PageImpl<>(List.of(accountDto));
        pageable = PageRequest.of(0, 10);

        existingId = 1L;
        nonExistingId = 99L;
        dependentId = 90L;

        Mockito.when(accountServiceMock.findAllPaged(pageable)).thenReturn(accountPage);

        Mockito.when(accountServiceMock.save(ArgumentMatchers.any())).thenReturn(accountDto);

        Mockito.when(accountServiceMock.update(existingId, accountDto)).thenReturn(accountDto);

        Mockito.doThrow(EmptyResultDataAccessException.class).when(accountServiceMock).delete(nonExistingId);

        Mockito.doThrow(DataIntegrityViolationException.class).when(accountServiceMock).delete(dependentId);
    }

    @Test
    @DisplayName("findAllPaged returns list of all accounts in page object when successful")
    void findAllPaged_ReturnsListOfAllAccountsInsidePage_WhenSuccessful() {
        ResponseEntity<Page<AccountDto>> page = accountController.findAllPaged(pageable);

        Page<AccountDto> body = page.getBody();

        Assertions.assertThat(Objects.requireNonNull(body).toList())
                .isNotEmpty()
                .isNotNull()
                .hasSize(1);

        Assertions.assertThat(page.getStatusCode()).isEqualTo(HttpStatus.OK);

        Assertions.assertThat(body.toList().get(0).getId())
                .isEqualTo(AccountDtoCreator.createAccountDto().getId());
    }

    @Test
    @DisplayName("save persists the account and returns an AccountDto when successful")
    void save_PersistsTheAccountAndReturnsAccountDto_WhenSuccessful() {
        ResponseEntity<AccountDto> accountSaved = accountController.save(accountDto);

        Assertions.assertThat(accountSaved.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Assertions.assertThat(accountSaved.getBody())
                .isNotNull()
                .isInstanceOf(AccountDto.class)
                .isEqualTo(AccountDtoCreator.createAccountDto());
    }

    @Test
    @DisplayName("delete removes account when successful")
    void delete_RemovesAccount_WhenSuccessful() {
        Assertions.assertThatCode(() -> {
                    accountController.delete(existingId);
                })
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = accountController.delete(existingId);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete throws EmptyResultDataAccessException when Id not found")
    void delete_ThrowsEmptyResultDataAccessException_WhenIdNotFound() {
        Assertions.assertThatThrownBy(() -> accountController.delete(nonExistingId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("delete throws DataIntegrityViolationException when Id has dependent object")
    void delete_ThrowsDataIntegrityViolationException_WhenIdHasDependentObjects() {
        Assertions.assertThatThrownBy(() -> accountController.delete(dependentId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("update replaces an account object with new information when successful")
    void update_ReplacesAccountObject_WhenSuccessful() {
        Assertions.assertThatCode(() -> {
                    accountController.update(existingId, accountDto);
                })
                .doesNotThrowAnyException();
    }
}