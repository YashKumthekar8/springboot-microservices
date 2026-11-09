package com.project.accounts.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(
        name="Accounts",
        description="Schema to hold Account information"
)
public class AccountsDTO {

    @NotEmpty(message = "Account Number can not be a null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Account Number must be 10 digits")
    @Schema(
            description="Account Number of Bank Account", example="9869123149"
    )
    private Long accountNumber;

    @NotEmpty(message = "Account Type can not be a null or empty")
    @Schema(
            description = "Account type of Bank account", example = "Savings"
    )
    private String accountType;

    @NotEmpty(message = "BranchAddress can not be a null or empty")
    @Schema(
            description = "Eazy Bank branch address", example = "123 NewYork"
    )
    private String branchAddress;


}
