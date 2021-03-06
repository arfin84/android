/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.tools.idea.uibuilder.scene;

import com.android.tools.idea.uibuilder.model.Coordinates;
import com.android.tools.idea.uibuilder.model.NlComponent;
import com.android.tools.idea.uibuilder.model.SwingCoordinate;
import com.android.tools.idea.uibuilder.surface.Interaction;
import com.android.tools.idea.uibuilder.surface.ScreenView;
import org.intellij.lang.annotations.JdkConstants;
import org.jetbrains.annotations.NotNull;

/**
 * Implements a mouse interaction started on a Scene
 */
public class SceneInteraction extends Interaction {

  /**
   * The surface associated with this interaction.
   */
  private final ScreenView myScreenView;

  /**
   * Base constructor
   *
   * @param screenView the ScreenView we belong to
   * @param component  the component we belong to
   */
  public SceneInteraction(@NotNull ScreenView screenView) {
    myScreenView = screenView;
  }

  /**
   * Start the mouse interaction
   *
   * @param x         The most recent mouse x coordinate applicable to this interaction
   * @param y         The most recent mouse y coordinate applicable to this interaction
   * @param startMask The initial AWT mask for the interaction
   */
  @Override
  public void begin(@SwingCoordinate int x, @SwingCoordinate int y, @JdkConstants.InputEventMask int startMask) {
    super.begin(x, y, startMask);
    int androidX = Coordinates.getAndroidX(myScreenView, myStartX);
    int androidY = Coordinates.getAndroidY(myScreenView, myStartY);
    int dpX = Coordinates.pxToDp(myScreenView, androidX);
    int dpY = Coordinates.pxToDp(myScreenView, androidY);
    Scene scene = myScreenView.getScene();
    scene.updateModifiers(startMask);
    scene.mouseDown(SceneContext.get(myScreenView), dpX, dpY);
  }

  /**
   * Update the mouse interaction
   *
   * @param x         The most recent mouse x coordinate applicable to this interaction
   * @param y         The most recent mouse y coordinate applicable to this interaction
   * @param modifiers current modifier key mask
   */
  @Override
  public void update(@SwingCoordinate int x, @SwingCoordinate int y, @JdkConstants.InputEventMask int modifiers) {
    super.update(x, y, modifiers);
    int androidX = Coordinates.getAndroidX(myScreenView, x);
    int androidY = Coordinates.getAndroidY(myScreenView, y);
    int dpX = Coordinates.pxToDp(myScreenView, androidX);
    int dpY = Coordinates.pxToDp(myScreenView, androidY);
    Scene scene = myScreenView.getScene();
    scene.updateModifiers(modifiers);
    scene.mouseDrag(SceneContext.get(myScreenView), dpX, dpY);
    myScreenView.getSurface().repaint();
  }

  /**
   * Ends the mouse interaction and commit the modifications if any
   *
   * @param x         The most recent mouse x coordinate applicable to this interaction
   * @param y         The most recent mouse y coordinate applicable to this interaction
   * @param modifiers current modifier key mask
   * @param canceled  True if the interaction was canceled, and false otherwise.
   */
  @Override
  public void end(@SwingCoordinate int x, @SwingCoordinate int y, @JdkConstants.InputEventMask int modifiers, boolean canceled) {
    super.end(x, y, modifiers, canceled);
    final int androidX = Coordinates.getAndroidX(myScreenView, x);
    final int androidY = Coordinates.getAndroidY(myScreenView, y);
    int dpX = Coordinates.pxToDp(myScreenView, androidX);
    int dpY = Coordinates.pxToDp(myScreenView, androidY);
    Scene scene = myScreenView.getScene();
    scene.updateModifiers(modifiers);
    scene.mouseRelease(SceneContext.get(myScreenView), dpX, dpY);
    myScreenView.getSurface().repaint();
  }
}
