package com.xendit.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.xendit.Xendit;
import com.xendit.enums.BankCode;
import com.xendit.exception.XenditException;
import com.xendit.network.BaseRequest;
import com.xendit.network.RequestResource;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class FixedVirtualAccountTest {
  private static Map<String, String> HEADERS = new HashMap<>();
  private static Map<String, Object> PARAMS = new HashMap<>();
  private static String URL = String.format("%s%s", Xendit.getUrl(), "/callback_virtual_accounts");
  private static String TEST_ID = "test_id";
  private static String TEST_EXTERNAL_ID = "test_external_id";
  private static BankCode TEST_BANK_CODE = BankCode.BNI;
  private static String TEST_NAME = "John Doe";
  private static FixedVirtualAccount VALID_ACCOUNT =
      FixedVirtualAccount.builder().id(TEST_ID).build();

  @Before
  public void initMocks() {
    PARAMS.clear();
    Xendit.requestClient = mock(BaseRequest.class);
    PARAMS.put("external_id", TEST_EXTERNAL_ID);
    PARAMS.put("bank_code", TEST_BANK_CODE);
    PARAMS.put("name", TEST_NAME);
  }

  @Test
  public void createClosed_Success_IfParamsAreValid() throws XenditException {
    PARAMS.put("expected_amount", 200000000);

    when(Xendit.requestClient.request(
            RequestResource.Method.POST, URL, HEADERS, PARAMS, FixedVirtualAccount.class))
        .thenReturn(VALID_ACCOUNT);
    FixedVirtualAccount fixedVirtualAccount = FixedVirtualAccount.createClosed(PARAMS);

    assertEquals(fixedVirtualAccount, VALID_ACCOUNT);
  }

  @Test(expected = XenditException.class)
  public void createClosed_ThrowsException_IfParamsAreInvalid() throws XenditException {
    PARAMS.put("expected_amount", 50000000001L);

    when(Xendit.requestClient.request(
            RequestResource.Method.POST, URL, HEADERS, PARAMS, FixedVirtualAccount.class))
        .thenThrow(new XenditException("Maximum amount is 50000000000"));
    FixedVirtualAccount.createClosed(PARAMS);
  }

  @Test
  public void createOpen_Success_IfParamsAreValid() throws XenditException {
    when(Xendit.requestClient.request(
            RequestResource.Method.POST, URL, HEADERS, PARAMS, FixedVirtualAccount.class))
        .thenReturn(VALID_ACCOUNT);
    FixedVirtualAccount fixedVirtualAccount = FixedVirtualAccount.createOpen(PARAMS);

    assertEquals(fixedVirtualAccount, VALID_ACCOUNT);
  }

  @Test(expected = XenditException.class)
  public void createOpen_ThrowsException_IfParamsAreInvalid() throws XenditException {
    PARAMS.put("bank_code", "XYZ");

    when(Xendit.requestClient.request(
            RequestResource.Method.POST, URL, HEADERS, PARAMS, FixedVirtualAccount.class))
        .thenThrow(new XenditException("That bank code is not currently supported"));
    FixedVirtualAccount.createOpen(PARAMS);
  }

  @Test
  public void getAvailableBanks_Success() throws XenditException {
    String url = String.format("%s%s", Xendit.getUrl(), "/available_virtual_account_banks");
    AvailableBank[] availableBanks = new AvailableBank[] {};

    when(Xendit.requestClient.request(RequestResource.Method.GET, url, null, AvailableBank[].class))
        .thenReturn(availableBanks);
    AvailableBank[] result = FixedVirtualAccount.getAvailableBanks();

    assertArrayEquals(availableBanks, result);
  }

  @Test
  public void getFixedVA_Success_IfIdIsAvailable() throws XenditException {
    String url = String.format("%s%s%s", URL, "/", TEST_ID);

    when(Xendit.requestClient.request(
            RequestResource.Method.GET, url, null, FixedVirtualAccount.class))
        .thenReturn(VALID_ACCOUNT);
    FixedVirtualAccount fixedVirtualAccount = FixedVirtualAccount.getFixedVA(TEST_ID);

    assertEquals(fixedVirtualAccount, VALID_ACCOUNT);
  }

  @Test(expected = XenditException.class)
  public void getFixedVA_ThrowsException_IfIdIsAvailable() throws XenditException {
    String url = String.format("%s%s", URL, "/fake_id");

    when(Xendit.requestClient.request(
            RequestResource.Method.GET, url, null, FixedVirtualAccount.class))
        .thenThrow(new XenditException("Callback virtual account not found"));
    FixedVirtualAccount.getFixedVA("fake_id");
  }

  @Test
  public void update_Success_IfIdIsAvailable() throws XenditException {
    FixedVirtualAccount fixedVirtualAccount =
        FixedVirtualAccount.builder().id(TEST_ID).isSingleUse(true).build();
    Map<String, Object> params = new HashMap<>();
    params.put("is_single_use", true);
    String url = String.format("%s%s%s", URL, "/", TEST_ID);

    when(Xendit.requestClient.request(
            RequestResource.Method.PATCH, url, params, FixedVirtualAccount.class))
        .thenReturn(fixedVirtualAccount);
    FixedVirtualAccount result = FixedVirtualAccount.update(TEST_ID, params);

    assertEquals(result, fixedVirtualAccount);
  }

  @Test(expected = XenditException.class)
  public void update_ThrowsException_IfIdIsNotAvailable() throws XenditException {
    Map<String, Object> params = new HashMap<>();
    String url = String.format("%s%s", URL, "/fake_id");

    when(Xendit.requestClient.request(
            RequestResource.Method.PATCH, url, params, FixedVirtualAccount.class))
        .thenThrow(new XenditException("Could not find callback virtual account"));
    FixedVirtualAccount.update("fake_id", params);
  }

  @Test
  public void getPayment_Success_IfIdIsAvailable() throws XenditException {
    FixedVirtualAccountPayment fixedVirtualAccountPayment =
        FixedVirtualAccountPayment.builder().id("test_id").build();
    String url =
        String.format(
            "%s%s", Xendit.getUrl(), "/callback_virtual_account_payments/payment_id=test_id");

    when(Xendit.requestClient.request(
            RequestResource.Method.GET, url, null, FixedVirtualAccountPayment.class))
        .thenReturn(fixedVirtualAccountPayment);
    FixedVirtualAccountPayment result = FixedVirtualAccount.getPayment("test_id");

    assertEquals(result, fixedVirtualAccountPayment);
  }
}
