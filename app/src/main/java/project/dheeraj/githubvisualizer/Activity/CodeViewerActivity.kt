/*
 * MIT License
 *
 * Copyright (c) 2020 Dheeraj Kotwani
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package project.dheeraj.githubvisualizer.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import kotlinx.android.synthetic.main.activity_code_viewer.*
import kotlinx.android.synthetic.main.content_repository_info.*
import project.dheeraj.githubvisualizer.R

class CodeViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_viewer)

        if (intent.hasExtra("url"))
            webView.loadUrl(intent.getStringExtra("url"))

//                repoWebView.setOnTouchListener(OnTouchListener { v, event -> event.action == MotionEvent.ACTION_MOVE })
        webView.isVerticalScrollBarEnabled = true
        webView.settings.javaScriptEnabled = true;
        webView.settings.domStorageEnabled = true
        webView.settings.setAppCacheEnabled(true);
        webView.settings.loadsImagesAutomatically = true;
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
        webView.settings.layoutAlgorithm =
            WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.settings.setSupportZoom(true)
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = false
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = true

    }
}
