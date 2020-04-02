// Generated code from Butter Knife. Do not modify!
package com.flow.lab6.networkmanagement;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  private View view7f070045;

  private View view7f0700b6;

  private View view7f070078;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(final MainActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.checkConnBtn, "field 'checkConnBtn' and method 'CheckConnectivity'");
    target.checkConnBtn = Utils.castView(view, R.id.checkConnBtn, "field 'checkConnBtn'", Button.class);
    view7f070045 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.CheckConnectivity(Utils.castParam(p0, "doClick", 0, "CheckConnectivity", 0, Button.class));
      }
    });
    view = Utils.findRequiredView(source, R.id.weatherBtn, "field 'getWeatherBtn' and method 'setGetWeatherBtn'");
    target.getWeatherBtn = Utils.castView(view, R.id.weatherBtn, "field 'getWeatherBtn'", Button.class);
    view7f0700b6 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.setGetWeatherBtn(Utils.castParam(p0, "doClick", 0, "setGetWeatherBtn", 0, Button.class));
      }
    });
    view = Utils.findRequiredView(source, R.id.parseJsonBtn, "field 'parseJsonBtn' and method 'setParseJsonBtn'");
    target.parseJsonBtn = Utils.castView(view, R.id.parseJsonBtn, "field 'parseJsonBtn'", Button.class);
    view7f070078 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.setParseJsonBtn(Utils.castParam(p0, "doClick", 0, "setParseJsonBtn", 0, Button.class));
      }
    });
    target.parsedTxt = Utils.findRequiredViewAsType(source, R.id.parsedTxt, "field 'parsedTxt'", TextView.class);
    target.cityInput = Utils.findRequiredViewAsType(source, R.id.weatherInfoTxt, "field 'cityInput'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.checkConnBtn = null;
    target.getWeatherBtn = null;
    target.parseJsonBtn = null;
    target.parsedTxt = null;
    target.cityInput = null;

    view7f070045.setOnClickListener(null);
    view7f070045 = null;
    view7f0700b6.setOnClickListener(null);
    view7f0700b6 = null;
    view7f070078.setOnClickListener(null);
    view7f070078 = null;
  }
}
