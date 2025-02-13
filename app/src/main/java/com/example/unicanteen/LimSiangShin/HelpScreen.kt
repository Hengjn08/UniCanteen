import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unicanteen.R
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.navigation.NavigationDestination

object HelpDestination : NavigationDestination {
    override val route = "Help?userId={userId}"
    override val title = "Help"
    fun routeWithArgs(userId: Int): String{
        return "${route}/$userId"
    }
}
@Composable
fun HelpScreen(
    navigateBack: () -> Unit,

) {
    Scaffold(
        topBar = {
            UniCanteenTopBar()
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                TitleWithTheNavigateBackButton(
                    title = "Help Center",
                    navigateBack = { navigateBack }
                )

                Text(
                    text = "Frequently Asked Questions",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp,
                    color = colorResource(R.color.orange_500),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                )
                HorizontalDivider(
                    thickness = 4.dp,
                    modifier = Modifier.fillMaxWidth(),
                    color = colorResource(R.color.orange_500),

                )
                Spacer(modifier = Modifier.height(16.dp))
                FAQItem(
                    question = "How can I reset my password?",
                    answer = "You can reset your password by going to the 'Forgot Password' section on the login page. Or you also can go to the profile screen for clicking the edit icon.",
                    imageIds = listOf(R.drawable.forgot_password, R.drawable.edit)
                )
                FAQItem(
                    question = "How do I update my profile?",
                    answer = "To update your profile, navigate to your profile section and click on edit icon.",
                    imageIds = emptyList()
                )
                FAQItem(
                    question = "How do I contact customer support?",
                    answer = "You can reach out to our customer support via email at support@example.com or call us at 123-456-789.",
                    imageIds = emptyList()
                )
                FAQItem(
                    question = "How do I track my order?",
                    answer = "You can track your order by navigating to the 'Order List' section, To navigate to the 'Order List' section just click on the " + Icons.Default.List + "Order List button.",
                    imageIds = listOf(R.drawable.help_track_order)
                )
                FAQItem(
                    question = "Can I cancel my order?",
                    answer = "Yes, you can cancel your order within 30 minutes at the shop that you make the order.",
                    imageIds = emptyList()
                )
                FAQItem(
                    question = "What payment methods are accepted?",
                    answer = "We accept payments through TnG App, Online Banking and pay Cash at the counter.",
                    imageIds = listOf(R.drawable.touch_ngo_icon, R.drawable.bank_logo)
                )
                FAQItem(
                    question = "How do I report an issue with my order?",
                    answer = "You can report any issues by contacting our support team via email at support@example.com or through the 'Report Issue' button on your order details page.",
                    imageIds = emptyList()
                )

                FAQItem(
                    question = "Why was my payment declined?",
                    answer = "Payments can be declined for various reasons. Please check with your bank or payment provider to ensure there are no issues with your account.",
                    imageIds = emptyList()
                )

                FAQItem(
                    question = "Is my personal information secure?",
                    answer = "Yes, we take your privacy seriously. All your personal information is encrypted and securely stored.",
                    imageIds = emptyList()
                )
                FAQItem(
                    question = "How do I change my notification settings?",
                    answer = "You can change your notification preferences in 'Settings' under 'Notifications'.",
                    imageIds = emptyList()
                )
                FAQItem(
                    question = "What should I do if I receive the wrong order?",
                    answer = "If you receive the wrong order, please contact our customer support immediately for assistance.",
                    imageIds = emptyList()
                )
                FAQItem(
                    question = "How can I become a seller?",
                    answer = "To become a seller, you need to contact the office to register your business.",
                    imageIds = emptyList()
                )
            }
        }
    )
}

@Composable
fun FAQItem(question: String, answer: String, imageIds: List<Int>) {
    var isExpanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        Row{
            ClickableText(
                text = AnnotatedString(question),
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                onClick = { isExpanded = !isExpanded }
            )
            Icon(
                imageVector = if(isExpanded)Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = "Show description",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(AbsoluteAlignment.Right)
                    .clickable { isExpanded = !isExpanded },

            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isExpanded) {
            Text(
                text = answer,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            )

            // Display images if any are provided
            if (imageIds.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    imageIds.forEach { imageId ->
                        Image(
                            painter = painterResource(id = imageId),
                            contentDescription = "Solution Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
        Divider()
    }
}

@Composable
fun TitleWithTheNavigateBackButton(
    title: String,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        IconButton(onClick = navigateBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = modifier.size(40.dp)
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 32.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun HelpScreenPreview() {
    HelpScreen(navigateBack = {})
}
