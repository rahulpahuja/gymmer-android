package com.m1x.gymmer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.m1x.gymmer.R
import com.m1x.gymmer.ui.components.*
import com.m1x.gymmer.ui.screens.state.LoginUiState
import com.m1x.gymmer.ui.screens.state.UserRole
import com.m1x.gymmer.ui.screens.viewmodel.LoginViewModel
import com.m1x.gymmer.ui.theme.GymmerTheme
import com.m1x.gymmer.ui.theme.LimeGreen

@Composable
fun LoginScreen(
    navController: androidx.navigation.NavController? = null,
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: (UserRole) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LoginContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChanged,
        onPasswordChange = viewModel::onPasswordChanged,
        onRoleChange = viewModel::onRoleChanged,
        onLoginClick = { viewModel.login(onLoginSuccess) },
        onTestClick = { navController?.navigate("test") }
    )
}

@Composable
fun LoginContent(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRoleChange: (UserRole) -> Unit,
    onLoginClick: () -> Unit,
    onTestClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = "GYMOPS",
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            ),
            color = Color.White,
            modifier = Modifier.clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = null,
                onClick = onTestClick
            )
        )
        Text(
            text = "KINETIC VOLT",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            letterSpacing = 4.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        SecondaryTabRow(
            selectedTabIndex = if (uiState.selectedRole == UserRole.TRAINEE) 0 else 1,
            containerColor = Color.Black,
            contentColor = LimeGreen,
            divider = {},
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(if (uiState.selectedRole == UserRole.TRAINEE) 0 else 1),
                    color = LimeGreen
                )
            }
        ) {
            Tab(
                selected = uiState.selectedRole == UserRole.TRAINEE,
                onClick = { onRoleChange(UserRole.TRAINEE) },
                text = { Text("TRAINEE", style = MaterialTheme.typography.labelLarge) }
            )
            Tab(
                selected = uiState.selectedRole == UserRole.TRAINER,
                onClick = { onRoleChange(UserRole.TRAINER) },
                text = { Text("TRAINER", style = MaterialTheme.typography.labelLarge) }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = if (uiState.selectedRole == UserRole.TRAINEE) stringResource(R.string.login_trainee_title) else "Trainer Login",
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.login_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(40.dp))

        GymTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = stringResource(R.string.login_email_label),
            placeholder = stringResource(R.string.login_email_placeholder),
            leadingIcon = Icons.Default.Email,
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            GymTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = stringResource(R.string.login_password_label),
                placeholder = stringResource(R.string.login_password_placeholder),
                leadingIcon = Icons.Default.Lock,
                isPassword = true,
                enabled = !uiState.isLoading
            )
            Text(
                text = stringResource(R.string.login_forgot),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = LimeGreen,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 2.dp)
            )
        }

        if (uiState.error != null) {
            Text(
                text = uiState.error,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        GymButton(
            text = if (uiState.isLoading) stringResource(R.string.login_processing) else stringResource(R.string.login_initiate),
            onClick = onLoginClick,
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        GymSecondaryButton(
            text = stringResource(R.string.login_biometric),
            onClick = { /* TODO */ },
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.login_social_auth),
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SocialAuthButton(
                text = "Google",
                modifier = Modifier.weight(1f)
            )
            SocialAuthButton(
                text = "Apple",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row {
            Text(
                text = stringResource(R.string.login_new_user),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = stringResource(R.string.login_apply_onboarding),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = LimeGreen,
                textDecoration = TextDecoration.Underline
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SocialAuthButton(
    text: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.size(24.dp).background(Color.Gray, CircleShape))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun LoginScreenPreview() {
    GymmerTheme {
        LoginContent(
            uiState = LoginUiState(),
            onEmailChange = {},
            onPasswordChange = {},
            onRoleChange = {},
            onLoginClick = {}
        )
    }
}
