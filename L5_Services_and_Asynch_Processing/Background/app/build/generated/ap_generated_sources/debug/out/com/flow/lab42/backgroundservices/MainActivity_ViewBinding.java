// Generated code from Butter Knife. Do not modify!
package com.flow.lab42.backgroundservices;

import android.view.View;
import android.widget.Button;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(MainActivity target, View source) {
    this.target = target;

    target.startServBtn = Utils.findRequiredViewAsType(source, R.id.startServBtn, "field 'startServBtn'", Button.class);
    target.stopServBtn = Utils.findRequiredViewAsType(source, R.id.stopServBtn, "field 'stopServBtn'", Button.class);
    target.bindServBtn = Utils.findRequiredViewAsType(source, R.id.bindServBtn, "field 'bindServBtn'", Button.class);
    target.unbindServBtn = Utils.findRequiredViewAsType(source, R.id.unbindServBtn, "field 'unbindServBtn'", Button.class);
    target.getStateBtn = Utils.findRequiredViewAsType(source, R.id.getStateBtn, "field 'getStateBtn'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.startServBtn = null;
    target.stopServBtn = null;
    target.bindServBtn = null;
    target.unbindServBtn = null;
    target.getStateBtn = null;
  }
}
