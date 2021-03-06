package com.xendit.model;

import com.xendit.Xendit;
import com.xendit.exception.XenditException;
import com.xendit.network.RequestResource;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;

@Builder
public class RetailOutlet {
  /**
   * Create fixed payment code with all parameters as HashMap
   *
   * @param params listed here https://xendit.github.io/apireference/#update-fixed-payment-code
   * @return FixedPaymentCodeRetailOutlet
   * @throws XenditException XenditException
   */
  public static FixedPaymentCode createFixedPaymentCode(Map<String, Object> params)
      throws XenditException {
    return createFixedPaymentCode(new HashMap<>(), params);
  }

  /**
   * Create fixed payment code with all parameters as HashMap
   *
   * @param headers
   * @param params listed here https://xendit.github.io/apireference/#update-fixed-payment-code
   * @return FixedPaymentCodeRetailOutlet
   * @throws XenditException XenditException
   */
  public static FixedPaymentCode createFixedPaymentCode(
      Map<String, String> headers, Map<String, Object> params) throws XenditException {
    String url = String.format("%s%s", Xendit.getUrl(), "/fixed_payment_code");
    return Xendit.requestClient.request(
        RequestResource.Method.POST, url, headers, params, FixedPaymentCode.class);
  }

  /**
   * Create fixed payment code with required parameters
   *
   * @param externalId An ID of your choice. Often it is unique identifier like a phone number,
   *     email or transaction ID. Maximum length allowed is 1000 characters.
   * @param retailOutletName Name of the fixed payment code you want to create.
   * @param name Name of user - this might be used by the Retail Outlets cashier to validate the end
   *     user.
   * @param expectedAmount The amount that is expected to be paid by end customer.
   * @return FixedPaymentCodeRetailOutlet
   * @throws XenditException XenditException
   */
  public static FixedPaymentCode createFixedPaymentCode(
      String externalId,
      FixedPaymentCode.RetailOutletName retailOutletName,
      String name,
      Number expectedAmount)
      throws XenditException {
    String url = String.format("%s%s", Xendit.getUrl(), "/fixed_payment_code");
    Map<String, Object> params = new HashMap<>();
    params.put("external_id", externalId);
    params.put("retail_outlet_name", retailOutletName);
    params.put("name", name);
    params.put("expected_amount", expectedAmount);
    return Xendit.requestClient.request(
        RequestResource.Method.POST, url, params, FixedPaymentCode.class);
  }

  /**
   * Get fixed payment code by ID
   *
   * @param id ID of the fixed payment code to retrieve
   * @return FixedPaymentCode
   * @throws XenditException XenditException
   */
  public static FixedPaymentCode getFixedPaymentCode(String id) throws XenditException {
    return getFixedPaymentCode(id, new HashMap<>());
  }

  /**
   * Get fixed payment code by ID
   *
   * @param id ID of the fixed payment code to retrieve
   * @param headers
   * @return FixedPaymentCode
   * @throws XenditException XenditException
   */
  public static FixedPaymentCode getFixedPaymentCode(String id, Map<String, String> headers)
      throws XenditException {
    String url = String.format("%s%s%s", Xendit.getUrl(), "/fixed_payment_code/", id);
    return Xendit.requestClient.request(
        RequestResource.Method.GET, url, headers, null, FixedPaymentCode.class);
  }

  /**
   * Update fixed payment code by ID and with all parameters as HashMap
   *
   * @param id ID of the fixed payment code to be updated
   * @param params listed here https://xendit.github.io/apireference/#update-fixed-payment-code
   * @return FixedPaymentCode
   * @throws XenditException XenditException
   */
  public static FixedPaymentCode updateFixedPaymentCode(String id, Map<String, Object> params)
      throws XenditException {
    return updateFixedPaymentCode(id, new HashMap<>(), params);
  }

  /**
   * Update fixed payment code by ID and with all parameters as HashMap
   *
   * @param id ID of the fixed payment code to be updated
   * @param headers
   * @param params listed here https://xendit.github.io/apireference/#update-fixed-payment-code
   * @return FixedPaymentCode
   * @throws XenditException XenditException
   */
  public static FixedPaymentCode updateFixedPaymentCode(
      String id, Map<String, String> headers, Map<String, Object> params) throws XenditException {
    String url = String.format("%s%s%s", Xendit.getUrl(), "/fixed_payment_code/", id);
    return Xendit.requestClient.request(
        RequestResource.Method.PATCH, url, headers, params, FixedPaymentCode.class);
  }

  /**
   * Update fixed payment code by ID and with parameters
   *
   * @param id ID of the fixed payment code to be updated
   * @param name Name for the fixed payment code
   * @param expectedAmount The amount that is expected to be paid by end customer
   * @param expirationDate The time when the fixed payment code will be expired. You can set it to
   *     be days in the past to expire fixed payment code immediately
   * @return FixedPaymentCode
   * @throws XenditException XenditException
   */
  public static FixedPaymentCode updateFixedPaymentCode(
      String id, String name, Number expectedAmount, String expirationDate) throws XenditException {
    String url = String.format("%s%s%s", Xendit.getUrl(), "/fixed_payment_code/", id);
    Map<String, Object> params = new HashMap<>();
    if (name != null) params.put("name", name);
    if (expectedAmount != null) params.put("expected_amount", expectedAmount);
    if (expirationDate != null) params.put("expiration_date", expirationDate);
    return Xendit.requestClient.request(
        RequestResource.Method.PATCH, url, params, FixedPaymentCode.class);
  }
}
