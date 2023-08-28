import com.codeborne.selenide.SelenideElement;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.page;

import java.net.URL;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TemplateSteps {

    private static LoginPage loginPage;
    private static DashboardPage dashboardPage;
    private static VerificationPage verificationPage;

    @Пусть("открыта страница с формой авторизации \"([^\"]*)\"")
    public void openAuthPage(String url) {
        loginPage = open(url, LoginPage.class);
    }

    @Пусть("пользователь залогинен с именем \"([^\"]*)\" и паролем \"([^\"]*)\"")

    public void loginWithNameAndPassword(String name, String password) {

        $("[data-test-id=login] input").setValue(name);
        $("[data-test-id=password] input").setValue(password);
        $("[data-test-id=action-login]").click();

    }

    @Пусть("пользователь вводит проверочный код \'из смс\' \"([^\"]*)\"")
    public void setValidCode(String code) {
        $("[data-test-id=code] input").shouldBe(visible);
        $("[data-test-id=code] input").setValue(code);
        $("[data-test-id=action-verify]").click();
    }


    @Когда("пользователь переводит \"([^\"]*)\" р с карты с номером \"([^\"]*)\" на свою 1 карту с главной страницы")
    public void userTransferMoneyFromHisCard(int amount, String numberOfCard) {

        var firstCardNumber = DataHelper.getFirstCardNumber();

        var firstCardInfo = DataHelper.getFirstCardInfo();
        var secondCardInfo = DataHelper.getSecondCardInfo();


        if (firstCardNumber == numberOfCard) {
            var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
            dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
        } else {
            var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
            dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), secondCardInfo);
        }

    }

    @Тогда("баланс его 1 карты из списка на главной странице должен стать \"([^\"]*)\" рублей")
    public void successfulTransferAndIncreaseOfBalance(int finalAmount) {

        var firstCardInfo = DataHelper.getFirstCardInfo();
        assertEquals(15000, dashboardPage.getCardBalance(firstCardInfo));
    }
}

