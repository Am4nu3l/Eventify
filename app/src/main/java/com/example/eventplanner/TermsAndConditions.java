package com.example.eventplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class TermsAndConditions extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_terms_and_conditions);
//    if(Build.VERSION.SDK_INT>=23){
//      View decore=TermsAndConditions.this.getWindow().getDecorView();
//      if(decore.getSystemUiVisibility()!=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){
//        decore.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//      }
//      else{
//        decore.setSystemUiVisibility(0);
//      }
//    }
    TextView textView=findViewById(R.id.terms);
    String termsAndConditions = "Terms and Conditions for [Eventify]\n\n"
            + "Effective Date: [2024/5/24]\n\n"
            + "Please read these Terms and Conditions (\"Agreement\") carefully before using [Your App Name] (\"the App\") operated by [Your Company Name] (\"the Company\").\n\n"
            + "By downloading, installing, accessing, or using the App, you agree to be bound by these Terms and Conditions. If you do not agree with any part of this Agreement, you must not use the App.\n\n"
            + "1. Payment Services\n\n"
            + "1.1 The App offers paid products or services (\"Paid Services\") that require payment through various means, including credit cards, mobile payments, or other accepted payment methods.\n\n"
            + "1.2 By purchasing any Paid Services, you agree to pay the specified fees as indicated in the App. The Company reserves the right to modify the pricing and availability of Paid Services at any time, with prior notice to users.\n\n"
            + "1.3 The Company utilizes secure third-party payment processors to handle all payment transactions. Your payment information is subject to the terms and privacy policies of these payment processors. The Company does not store or have access to your payment card details.\n\n"
            + "2. Refunds and Cancellations\n\n"
            + "2.1 Refunds may be available for certain Paid Services, subject to the refund policy outlined in the App and any applicable laws. The Company reserves the right to determine eligibility for refunds on a case-by-case basis.\n\n"
            + "2.2 To request a refund or cancellation, please follow the instructions provided in the App. The Company will process refund requests in accordance with its refund policy.\n\n"
            + "3. User Responsibilities\n\n"
            + "3.1 You agree to provide accurate, complete, and up-to-date payment information when making a purchase through the App. The Company is not responsible for any errors, delays, or failures in payment processing due to inaccurate or outdated payment information.\n\n"
            + "3.2 You are solely responsible for any fees, charges, or taxes associated with your use of the Paid Services, including any applicable sales taxes or value-added taxes.\n\n"
            + "4. Intellectual Property\n\n"
            + "4.1 The App and all its contents, including but not limited to text, graphics, logos, icons, images, audio/video clips, software, and other materials, are the intellectual property of the Company or its licensors and are protected by copyright, trademark, and other intellectual property laws.\n\n"
            + "4.2 You may not modify, reproduce, distribute, display, publish, transmit, license, create derivative works from, or sell any information, software, products, or services obtained from the App without the prior written consent of the Company.\n\n"
            + "5. Disclaimer of Liability\n\n"
            + "5.1 The Company makes reasonable efforts to ensure the accuracy and reliability of the App's content and functionality. However, the App is provided on an \"as is\" and \"as available\" basis, without any warranties or representations of any kind, whether express or implied.\n\n"
            + "5.2 The Company shall not be liable for any damages, including but not limited to direct, indirect, incidental, consequential, or punitive damages arising out of or in connection with your use of the App, including any issues related to payment processing.\n\n"
            + "6. Governing Law and Jurisdiction\n\n"
            + "6.1 This Agreement shall be governed by and construed in accordance with the laws of [Jurisdiction]. Any legal actions or proceedings arising out of or relating to this Agreement shall be brought exclusively in the courts of [Jurisdiction].";
    textView.setText(termsAndConditions);

  }
}
