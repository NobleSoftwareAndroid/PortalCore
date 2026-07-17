/**
 * Copyright (C) 2020 Wasabeef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * See about document.execCommand: https://developer.mozilla.org/en-US/docs/Web/API/Document/execCommand
 */

var RE = {};

RE.currentSelection = {
    "startContainer": 0,
    "startOffset": 0,
    "endContainer": 0,
    "endOffset": 0};

RE.editor = document.getElementById('editor');
RE.isPreventPaste = false
RE.isPreventCopyOrCut = false
RE.maxLength = -1;

document.addEventListener("selectionchange", function() { RE.backuprange(); });

// Initializations
RE.callback = function() {
    window.location.href = "re-callback://" + encodeURIComponent(RE.getHtml());
}

RE.callbackPaste = function(e) {
    window.location.href = "re-callback-paste://" + encodeURIComponent(e);
}

RE.callbackCopyOrCut = function(e) {
    window.location.href = "re-callback-copy-cut://" + encodeURIComponent(e);
}

RE.setHtml = function(contents) {
    RE.editor.innerHTML = decodeURIComponent(contents.replace(/\+/g, '%20'));
}

RE.getHtml = function() {
    return RE.editor.innerHTML;
}

RE.getText = function() {
    return RE.editor.innerText;
}

RE.getLength = function() {
    var html = RE.editor.innerHTML;
    if (html === '<br>' || html === '<div><br></div>') {
        return 0;
    }
    return RE.calculateLogicalLength(html);
}

RE.getSelectedLength = function() {
    var selection = window.getSelection();
    if (selection.rangeCount > 0) {
        var range = selection.getRangeAt(0);
        var contents = range.cloneContents();
        var temp = document.createElement('div');
        temp.appendChild(contents);
        return RE.calculateLogicalLength(temp.innerHTML);
    }
    return 0;
}

RE.calculateLogicalLength = function(html) {
    if (!html) return 0;
    return html.length;
}

RE.setBaseTextColor = function(color) {
    RE.editor.style.color  = color;
}

RE.setBaseFontSize = function(size) {
    RE.editor.style.fontSize = size;
}

RE.setPadding = function(left, top, right, bottom) {
  RE.editor.style.paddingLeft = left;
  RE.editor.style.paddingTop = top;
  RE.editor.style.paddingRight = right;
  RE.editor.style.paddingBottom = bottom;
}

RE.setBackgroundColor = function(color) {
    document.body.style.backgroundColor = color;
}

RE.setBackgroundImage = function(image) {
    RE.editor.style.backgroundImage = image;
}

RE.setWidth = function(size) {
    RE.editor.style.minWidth = size;
}

RE.setHeight = function(size) {
    RE.editor.style.height = size;
}

RE.setTextAlign = function(align) {
    RE.editor.style.textAlign = align;
}

RE.setVerticalAlign = function(align) {
    RE.editor.style.verticalAlign = align;
}

RE.setPlaceholder = function(placeholder) {
    RE.editor.setAttribute("placeholder", placeholder);
}

RE.setInputEnabled = function(inputEnabled) {
    RE.editor.contentEditable = String(inputEnabled);
}

RE.undo = function() {
    document.execCommand('undo', false, null);
}

RE.redo = function() {
    document.execCommand('redo', false, null);
}

RE.exec = function(command, value = null) {
    var oldHtml = RE.editor.innerHTML;
    if (value) {
        document.execCommand(command, false, value);
    } else {
        document.execCommand(command, false, null);
    }
    if (RE.maxLength > 0 && RE.getLength() > RE.maxLength) {
        // If it exceeds, undo the last command
        document.execCommand('undo', false, null);
    }
    RE.callback();
}

RE.setBold = function() {
    RE.exec('bold');
}

RE.setItalic = function() {
    RE.exec('italic');
}

RE.setSubscript = function() {
    RE.exec('subscript');
}

RE.setSuperscript = function() {
    RE.exec('superscript');
}

RE.setStrikeThrough = function() {
    RE.exec('strikeThrough');
}

RE.setUnderline = function() {
    RE.exec('underline');
}

RE.setBullets = function() {
    RE.exec('insertUnorderedList');
}

RE.setNumbers = function() {
    RE.exec('insertOrderedList');
}

RE.setTextColor = function(color) {
    RE.restorerange();
    document.execCommand("styleWithCSS", null, true);
    RE.exec('foreColor', color);
    document.execCommand("styleWithCSS", null, false);
}

RE.setTextBackgroundColor = function(color) {
    RE.restorerange();
    document.execCommand("styleWithCSS", null, true);
    RE.exec('hiliteColor', color);
    document.execCommand("styleWithCSS", null, false);
}

RE.setFontSize = function(fontSize){
    RE.exec("fontSize", fontSize);
}

RE.setFontTextSize = function(size) {
    RE.restorerange();
    document.execCommand("styleWithCSS", null, true);
    RE.exec('fontSize', (size/4) + 'px');
    document.execCommand("styleWithCSS", null, false);
}

RE.setHeading = function(heading) {
    RE.exec('formatBlock', '<h'+heading+'>');
}

RE.setIndent = function() {
    RE.exec('indent');
}

RE.setOutdent = function() {
    RE.exec('outdent');
}

RE.setJustifyLeft = function() {
    RE.exec('justifyLeft');
}

RE.setJustifyCenter = function() {
    RE.exec('justifyCenter');
}

RE.setJustifyRight = function() {
    RE.exec('justifyRight');
}

RE.setBlockquote = function() {
    RE.exec('formatBlock', '<blockquote>');
}

RE.insertImage = function(url, alt) {
    var html = '<img src="' + url + '" alt="' + alt + '" />';
    RE.insertHTML(html);
}

RE.insertImageWithSize = function(url, alt="", width="", height="", relative="false") {
    var img = document.createElement('img');
    img.setAttribute("src", url);
    if (alt != "") img.setAttribute("alt", alt);
    if (relative == "true") {
       if (width == "") width = "100%";
         if (height == "")
             img.setAttribute("style","width: "+width+";");
         else
             img.setAttribute("style","width: "+width+"; height: "+height+";");
    } else {
       // for % of image size, width must be empty!
       if ((width.search("%") != -1) && (height == "")) {
          height = width;
          width = "";
       }
       img.setAttribute("width", width);
       img.setAttribute("height", height);
    }
    img.onload = RE.updateHeight;
    RE.insertHTML(img.outerHTML);
    //RE.callback("input");
};

RE.insertImageW = function(url, alt, width) {
    var html = '<img src="' + url + '" alt="' + alt + '" width="' + width + '"/>';
    RE.insertHTML(html);
}

RE.insertImageWH = function(url, alt, width, height) {
    var html = '<img src="' + url + '" alt="' + alt + '" width="' + width + '" height="' + height +'"/>';
    RE.insertHTML(html);
}

RE.insertVideo = function(url, alt) {
    var html = '<video src="' + url + '" controls></video><br>';
    RE.insertHTML(html);
}

RE.insertVideoW = function(url, width) {
    var html = '<video src="' + url + '" width="' + width + '" controls></video><br>';
    RE.insertHTML(html);
}

RE.insertVideoWH = function(url, width, height) {
    var html = '<video src="' + url + '" width="' + width + '" height="' + height + '" controls></video><br>';
    RE.insertHTML(html);
}

RE.insertAudio = function(url, alt) {
    var html = '<audio src="' + url + '" controls></audio><br>';
    RE.insertHTML(html);
}

RE.insertYoutubeVideo = function(url) {
    var html = '<iframe width="100%" height="100%" src="' + url + '" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe><br>'
    RE.insertHTML(html);
}

RE.insertYoutubeVideoW = function(url, width) {
    var html = '<iframe width="' + width + '" src="' + url + '" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe><br>'
    RE.insertHTML(html);
}

RE.insertYoutubeVideoWH = function(url, width, height) {
    var html = '<iframe width="' + width + '" height="' + height + '" src="' + url + '" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe><br>'
    RE.insertHTML(html);
}

RE.insertHtmlValue =  function(htmlString) {
    RE.restorerange();

    if (RE.maxLength > 0) {
        var insertedLength = RE.calculateLogicalLength(htmlString);

        var currentLength = RE.getLength();
        var selectedLength = RE.getSelectedLength();
        if (currentLength - selectedLength + insertedLength > RE.maxLength) {
            return;
        }
    }

    var selection = window.getSelection();
    if (selection.rangeCount > 0) {
        var range = selection.getRangeAt(0);

        var tempDiv = document.createElement('div');
        tempDiv.innerHTML = htmlString;

        while (tempDiv.firstChild) {
            range.insertNode(tempDiv.firstChild);
        }

        range.collapse(false);
        selection.removeAllRanges();
        selection.addRange(range);
    }

    RE.focus();
    RE.callback();
}

RE.insertHTML = function(html) {
    RE.restorerange();

    if (RE.maxLength > 0) {
        var insertedLength = RE.calculateLogicalLength(html);

        var currentLength = RE.getLength();
        var selectedLength = RE.getSelectedLength();
        if (currentLength - selectedLength + insertedLength > RE.maxLength) {
            return;
        }
    }

    document.execCommand('insertHTML', false, html);
    RE.callback();
}

RE.insertLink = function(url, title) {
    RE.restorerange();
    var sel = document.getSelection();
    if (sel.toString().length == 0) {
        RE.insertHTML("<a href='"+url+"'>"+title+"</a>");
    } else if (sel.rangeCount) {
       var el = document.createElement("a");
       el.setAttribute("href", url);
       el.setAttribute("title", title);

       var range = sel.getRangeAt(0).cloneRange();
       range.surroundContents(el);
       if (RE.maxLength > 0 && RE.getLength() > RE.maxLength) {
           document.execCommand('undo', false, null);
       }
       sel.removeAllRanges();
       sel.addRange(range);
   }
    RE.callback();
}

RE.setTodo = function(text) {
    var html = '<input type="checkbox" name="'+ text +'" value="'+ text +'"/> &nbsp;';
    RE.insertHTML(html);
}

RE.prepareInsert = function() {
    RE.backuprange();
}

RE.backuprange = function(){
    var selection = window.getSelection();
    if (selection.rangeCount > 0) {
      var range = selection.getRangeAt(0);
      RE.currentSelection = {
          "startContainer": range.startContainer,
          "startOffset": range.startOffset,
          "endContainer": range.endContainer,
          "endOffset": range.endOffset};
    }
}

RE.restorerange = function(){
    var selection = window.getSelection();
    selection.removeAllRanges();
    var range = document.createRange();
    range.setStart(RE.currentSelection.startContainer, RE.currentSelection.startOffset);
    range.setEnd(RE.currentSelection.endContainer, RE.currentSelection.endOffset);
    selection.addRange(range);
}

RE.enabledEditingItems = function(e) {
    var items = [];
    if (document.queryCommandState('bold')) {
        items.push('bold');
    }
    if (document.queryCommandState('italic')) {
        items.push('italic');
    }
    if (document.queryCommandState('subscript')) {
        items.push('subscript');
    }
    if (document.queryCommandState('superscript')) {
        items.push('superscript');
    }
    if (document.queryCommandState('strikeThrough')) {
        items.push('strikeThrough');
    }
    if (document.queryCommandState('underline')) {
        items.push('underline');
    }
    if (document.queryCommandState('insertOrderedList')) {
        items.push('orderedList');
    }
    if (document.queryCommandState('insertUnorderedList')) {
        items.push('unorderedList');
    }
    if (document.queryCommandState('justifyCenter')) {
        items.push('justifyCenter');
    }
    if (document.queryCommandState('justifyFull')) {
        items.push('justifyFull');
    }
    if (document.queryCommandState('justifyLeft')) {
        items.push('justifyLeft');
    }
    if (document.queryCommandState('justifyRight')) {
        items.push('justifyRight');
    }
    if (document.queryCommandState('insertHorizontalRule')) {
        items.push('horizontalRule');
    }
    var formatBlock = document.queryCommandValue('formatBlock');
    if (formatBlock.length > 0) {
        items.push(formatBlock);
    }

    window.location.href = "re-state://" + encodeURI(items.join(','));
}

RE.focus = function() {
    var range = document.createRange();
    range.selectNodeContents(RE.editor);
    range.collapse(false);
    var selection = window.getSelection();
    selection.removeAllRanges();
    selection.addRange(range);
    RE.editor.focus();
}

RE.blurFocus = function() {
    RE.editor.blur();
}

RE.removeFormat = function() {
    document.execCommand('removeFormat', false, null);
}

RE.isSpecialKey = function(e) {
    var key = e.which || e.keyCode;
    return (
        key == 8 || // backspace
        key == 46 || // delete
        (key >= 37 && key <= 40) || // arrows
        e.ctrlKey || e.metaKey
    );
}

// Event Listeners
RE.editor.addEventListener("input", function(e) {
    if (RE.maxLength > 0 && RE.getLength() > RE.maxLength) {
        document.execCommand('undo', false, null);
    }
    RE.callback();
});
RE.editor.addEventListener("beforeinput", function(e) {
    if (RE.maxLength > 0) {
        if (e.inputType === 'insertText' || e.inputType === 'insertFromPaste') {
            var inserted = e.data || (e.dataTransfer && e.dataTransfer.getData('text/plain')) || "";
            var insertedLength = RE.calculateLogicalLength(inserted);
            var currentLength = RE.getLength();
            var selectedLength = RE.getSelectedLength();

            if (currentLength - selectedLength + insertedLength > RE.maxLength) {
                e.preventDefault();
            }
        }
    }
});
RE.editor.addEventListener("keydown", function(e) {
    if (RE.maxLength > 0) {
        var currentLength = RE.getLength();
        if (currentLength >= RE.maxLength && !RE.isSpecialKey(e)) {
            var selectionLength = RE.getSelectedLength();
            if (selectionLength == 0) {
                e.preventDefault();
            }
        }
    }
});
RE.editor.addEventListener("keyup", function(e) {
    var KEY_LEFT = 37, KEY_RIGHT = 39;
    if (e.which == KEY_LEFT || e.which == KEY_RIGHT) {
        RE.enabledEditingItems(e);
    }
});
RE.editor.addEventListener("click", RE.enabledEditingItems);

// Handle paste
RE.editor.addEventListener("paste", function (e) {
    var clipboardData, pastedData;
    clipboardData = e.clipboardData || window.clipboardData;
    pastedData = clipboardData.getData('Text');

    if (RE.isPreventPaste == "true") {
        // Stop data actually being pasted into div
        e.stopPropagation();
        e.preventDefault();

        // callback paste
        RE.callbackPaste(pastedData)
    } else if (RE.maxLength > 0) {
        var currentLength = RE.getLength();
        var selectedLength = RE.getSelectedLength();
        var remaining = RE.maxLength - (currentLength - selectedLength);
        var pastedLogicalLength = RE.calculateLogicalLength(pastedData);

        if (remaining <= 0) {
            e.preventDefault();
        } else if (pastedLogicalLength > remaining) {
            e.preventDefault();
            var trimmed = "";
            var currentLogical = 0;
            for (var char of pastedData) {
                var charLogical = RE.calculateLogicalLength(char);
                if (currentLogical + charLogical <= remaining) {
                    trimmed += char;
                    currentLogical += charLogical;
                } else {
                    break;
                }
            }
            document.execCommand('insertText', false, trimmed);
        }
    }
});


// Handle copy
RE.editor.addEventListener("copy", function (e) {
    if (RE.isPreventCopyOrCut == "true") {
        var selection = window.getSelection();

        if (e.clipboardData) {
            e.clipboardData.setData('Text', "");
        }
        e.preventDefault();

        // callback copy
        RE.callbackCopyOrCut(selection.toString())
    }
});

// Handle cut
RE.editor.addEventListener("cut", function (e) {
    if (RE.isPreventCopyOrCut == "true") {
        var selection = window.getSelection();

        if (e.clipboardData) {
            e.clipboardData.setData('Text', "");
        }
        e.preventDefault();

        // callback copy
        RE.callbackCopyOrCut(selection.toString())
    }
});

RE.setPreventPaste = function(isPrevent) {
    RE.isPreventPaste = isPrevent;
}

RE.setPreventCopyOrCut = function(isPrevent) {
    RE.isPreventCopyOrCut = isPrevent;
}

RE.setMaxLength = function(size) {
    RE.maxLength = Number(size);
}