package io.github.sidvenu.mathjaxview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.Nullable;
import io.github.sidvenu.mathjaxview.R.styleable;

public class MathJaxView extends WebView {
   String a;
   String b;
   String c;

   public MathJaxView(Context var1, AttributeSet var2) {
      super(var1, var2);
      TypedArray var3;
      String var4 = (var3 = this.getContext().obtainStyledAttributes(var2, styleable.MathJaxView)).getString(styleable.MathJaxView_android_text);
      this.setConfig("MathJax.Hub.Config({    extensions: ['fast-preview.js'],    messageStyle: 'none',    \"fast-preview\": {      disabled: false    },    CommonHTML: {      linebreaks: { automatic: true, width: \"container\" }    },    tex2jax: {      inlineMath: [ ['$','$'] ],      displayMath: [ ['$$','$$'] ],      processEscapes: true    },    TeX: {      extensions: [\"file:///android_asset/MathJax/extensions/TeX/mhchem.js\"],      mhchem: {legacy: false}    }});");
      this.c = "TeX-MML-AM_CHTML";
      if (!TextUtils.isEmpty(var4)) {
         this.setText(var4);
      }

      var3.recycle();
      this.getSettings().setJavaScriptEnabled(true);
      this.getSettings().setCacheMode(-1);
      this.setBackgroundColor(0);
      this.setWebViewClient(new WebViewClient() {
         public void onPageFinished(WebView var1, String var2) {
            var1.loadUrl("javascript:MathJax.Hub.Queue(['Typeset',MathJax.Hub]);");
         }
      });
   }

   private String a(String var1) {
      return var1.replace("\n", "").replace(" ", "");
   }

   public boolean onTouchEvent(MotionEvent var1) {
      return false;
   }

   public void setConfig(String var1) {
      this.b = this.a(var1);
   }

   public void setPredefinedConfig(String var1) {
      this.c = var1;
   }

   public void setText(String var1) {
      this.a = var1;
      this.loadDataWithBaseURL("about:blank", "<html><head><style>body {    text-align: center;}</style><script type=\"text/x-mathjax-config\">" + this.b + "</script><script type=\"text/javascript\" async src=\"file:///android_asset/MathJax/MathJax.js?config=" + this.c + "\"></script></head><body>" + var1 + "</body></html>", "text/html", "utf-8", "");
   }

   @Nullable
   public String getText() {
      return this.a;
   }
}
