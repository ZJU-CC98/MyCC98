var canemcode=true;
var canimgcode=true;
var canmediacode=true;
var canimgsign=true;
var emotnum=72;
var isSimple=false;
var emotdir='file:///android_asset/emot/';
var tdclass;
/*
	2012.10.08(樱桃): 增加 Silverlight 支持
	2012.10.09(樱桃): 改变 Silverlight 解析方式，支持替代内容和可选参数
	2012.11.17(樱桃): 增加 tr td th 元素支持
	2013.01.03(樱桃): 增加 topic 元素支持
	2013.01.04(樱桃): 增加 board 元素支持
	2013.03.29(樱桃): 修改 Flash 代码，降低安全风险
*/

// 字符串的 format 函数
String.format = function () {
	if (arguments.length == 0)
		return null;

	var str = arguments[0];
	for (var i = 1; i < arguments.length; i++) {
		var re = new RegExp('\\{' + (i - 1) + '\\}', 'gm');
		str = str.replace(re, arguments[i]);
	}
	return str;
}


function searchubb(tagid, posttype, thetdclass) {
	tdclass = thetdclass;
	var UbbInnerHtml = $(tagid).innerHTML.replace(/\<br\>/g, "<BR>").replace(/\r\n/g, '');
	if (posttype == 3)
		$(tagid).innerHTML = ubb.noubb(ubbsigncode(UbbInnerHtml));
	else {
		var currubb = ubbcode(UbbInnerHtml);
		var preubb = currubb;
		for (var i = 1; i < 10; i++)
			if ((currubb = ubbcode(preubb)) != preubb)
				preubb = currubb;
			else
				break;
		$(tagid).innerHTML = ubb.noubb(ubb.code(ubb.noubb(currubb), '', 1));
	}
}

function ubbcode(str) {
	var pattern;
	//noubb
	pattern = /(^.*?)\[noubb\](.*?)\[\/noubb\](.*$)/i;
	while (pattern.test(str)) {
		var beforeNoubb = RegExp.$1;
		ubb.storage.noubb.extend([RegExp.$2]);
		var afterNoubb = RegExp.$3;
		str = beforeNoubb + '{noubb' + ubb.num['noubb'].toString() + '}' + afterNoubb;
		ubb.num['noubb']++;
	}
	// code
	pattern = /(^.*?)(\[code(.*?)\](.*?)\[\/code\])(.*$)/i;
	while (pattern.test(str)) {
		var beforeCode = RegExp.$1;
		var insideCode = RegExp.$2;
		var language = RegExp.$3;
		var codeSource = RegExp.$4;
		var afterCode = RegExp.$5;
		ubb.storage.codeSource.extend([codeSource + '\r\n']);
		ubb.storage.code.extend([ubb.color(ubb.code(codeSource, language, 0))])
		str = beforeCode + '{codes' + ubb.num['code'].toString() + '}' + afterCode;
		ubb.num['code']++;
	}
	//image,media,url,emtion,quote,color and so on.
	str = ubb.img(str, 1);
	str = ubb.file(str);
	str = ubb.media(str);
	str = ubb.emotion(str);
	str = ubb.quote(str);
	//str = ubb.noEdit(str);
	str = ubb.color(str);
	str = ubb.share(str);
	str = ubb.software(str);
	str = ubb.topic(str);
	str = ubb.board(str);
	str = ubb.user(str);
	str = ubb.pm(str);
	str = ubb.b(str);
	str = ubb.i(str);
	str = ubb.u(str);
	str = ubb.del(str);
	//str = ubb.cursor(str);
	str = ubb.english(str);
	str = ubb.size(str);
	str = ubb.align(str);
	str = ubb.list(str);
	str = ubb.table(str);
	str = ubb.tr(str);
	str = ubb.th(str);
	str = ubb.td(str);
	str = ubb.subscription(str);
	str = ubb.nothot(str);
	str = ubb.font(str);
	str = ubb.filter(str);
	str = ubb.url(str, false);
	return str;
}

/* 
 *Silverlight 解析代码 by 樱桃
 */

// Silverlight 代码
function ubbSilverlightCode(str) {

	var pattern = /\[SL=(.*?)\](.*?)\[\/SL\]/gi;
	return str.replace(pattern, ubbSilverlightSingleReplace);
}

// 单个匹配的替换功能
function ubbSilverlightSingleReplace(all, params, altUbb) {
	var altHtml = ubbcode(altUbb);

	var obj = ubbSilverlightGenerateObject(params);
	ubbSilverlightAttachAltHtml(obj, altHtml);
	ubbSilverlightSetOptions(obj);

	var html = ubbSilverlightBuidHtml(obj);

	return html;
}

// 生成最终的 Silverlight Object HTML 代码。
function ubbSilverlightBuidHtml(objInfo) {

	if (objInfo == null) {
		return "";
	}

	var result = Silverlight.createObject(
			objInfo.source, // Source
			null, // Parent ID
			null, // Object ID
			objInfo.params, // Optional Params
			null, // Event Handlers
			objInfo.initParams, // initParams, 
			null // context ID
		);

	return result;
}


// 为 Silverlight 对象附加替代 HTML。
function ubbSilverlightAttachAltHtml(objeInfo, altHtml) {
	if (objeInfo != null) {
		objeInfo.params.alt = altHtml;
	}
}


// 移除引号
function removequote(str) {
	var quetomatch = /^"(.*)"$/i.exec(str);
	if (quetomatch != null) {
		return quetomatch[1];
	}
	else {
		return str;
	}
}

// 设置必要的安全信息。
function ubbSilverlightSetOptions(objInfo) {
	if (objInfo != null) {

		// 删除替代文字
		if (objInfo.params.alt == "") {
			delete objInfo.params.alt;
		}

		// 安全限制
		objInfo.params.enablehtmlaccess = false;
		objInfo.params.allowHtmlPopupWindow = false;
	}
}

// Silverlight 参数解析
function ubbSilverlightGenerateObject(params) {

	var pattern = /("[^"]*"|[^\s,]*)\s*,\s*("[^"]"|[^\s,]*)(.*)/i;

	// 参数表 "allowHtmlPopupWindow", "autoUpgrade", "background", "enableautozoom", "enableCacheVisualization", "enableGPUAcceleration", "enablehtmlaccess", "enableNavigation", "enableRedrawRegions", "maxframerate", "minRuntimeVersion", "splashscreensource", "windowless"

	// 禁止参数：enablehtmlaccess allowHtmlPopupWindow 允许参数 width height
	var allowedParams = new Array("autoUpgrade", "background", "enableautozoom", "enableCacheVisualization", "enableGPUAcceleration", "enableNavigation", "enableRedrawRegions", "maxframerate", "minRuntimeVersion", "splashscreensource", "width", "height", "windowless");

	// 转换大小写
	for (var i = 0; i < allowedParams.length; i++) {
		allowedParams[i] = allowedParams[i].toLowerCase();
	}

	var match = pattern.exec(params);
	if (match != null) {

		var result = {};

		result.source = removequote(match[1]);
		result.initParams = removequote(match[2]);

		result.params = {};

		var otherParams = match[3];
		var otherPattern = /\s*,\s*("[^"]*"|[^\s=]*)\s*=\s*(.[^"]*"|[^\s,]*)/gi;

		var addParam = function (all, name, value) {
			var realname = removequote(name).toLowerCase();

			// 参数限制
			if (allowedParams.indexOf(realname) != -1) {
				result.params[removequote(name)] = removequote(value);
			}
			return all;
		}

		otherParams.replace(otherPattern, addParam);

		return result;

	}
	else {
		return null;
	}
}

function ubbsigncode(str) {
	var pattern = /(^.*?)\[noubb\](.*?)\[\/noubb\](.*$)/i;
	while (pattern.test(str)) {
		var beforeNoubb = RegExp.$1;
		ubb.storage.noubb.extend([RegExp.$2]);
		var afterNoubb = RegExp.$3;
		str = beforeNoubb + '{noubb' + ubb.num['noubb'].toString() + '}' + afterNoubb;
		ubb.num['noubb']++;
	}
	str = ubbSilverlightCode(str);
	str = ubb.img(str, 0);
	str = ubb.i(str);
	str = ubb.b(str);
	str = ubb.u(str);
	str = ubb.del(str);
	str = ubb.topic(str);
	str = ubb.board(str);
	//str = ubb.cursor(str);
	str = ubb.user(str);
	str = ubb.color(str);
	str = ubb.share(str);
	str = ubb.filter(str);
	
	str = ubb.url(str, true);
	return str;
}

var copy2cb = function (str) {
	if (!Browser.Engine.trident)
		return;
	str = str.replace(/<BR>/gi, "\r\n");
	str = str.replace(/&nbsp;/gi, " ");
	str = str.replace(/&lt;/gi, "<");
	str = str.replace(/&gt;/gi, ">");
	str = str.replace(/&amp;/gi, "&");
	window.clipboardData.setData('text', str);
	alert('代码已经复制到剪贴板');
}

var loadImg = function (target) {
	ubb.num['img']++;
	return '<img src="' + target + '" id="resizeable' + ubb.num['img'] + '" onload="resizeImg($(\'resizeable' + ubb.num['img'] + '\'))" border="0" />';
}

var resizeImg = function (obj) {
	var maxWidth = obj.getParent('td').getSize().x * 0.90;
	if (obj.getSize().x > maxWidth)
		obj.setStyle('width', maxWidth);
}

window.addEvent('domready', function () {
	var quoteDivs = $$('div.quoteMaxHeightDiv');
	$each(quoteDivs, function (quoteDiv) {
		if (quoteDiv.getSize().y > 150) {
			quoteDiv.setStyle('height', 150);
		}
	})
	var qmds = $$('div.userQmd');
	$each(qmds, function (qmd) {
		if (qmd.innerHTML.test('<img.*?>', 'i')) {
			if (qmd.getSize().y > 450) {
				//qmd.setStyle('height','450px');
				qmd.innerHTML = '<br /><span style="color:blue;">该用户qmd过长，已被My CC98 My Home，请及时修改。</span><br />';
			}
		} else {
			if (qmd.getSize().y > 250) {
				//qmd.setStyle('height','250px');
				qmd.innerHTML = '<br /><span style="color:blue;">该用户qmd过长，已被My CC98 My Home，请及时修改。</span><br />';
			}
		}
	})

	var resizeableImgs = $$('img.resizeable');
	$each(resizeableImgs, function (img) {
		resizeImg(img);
	})
	if ($('showAllImages')) {
		$('showAllImages').addEvent('click', function () {
			$$('a.clickloadImage').each(function (item) {
				(function () { item.innerHTML = loadImg(item.href); }).delay(100);
			})
		})
	}
})

var codeUbb = function (source, language) {
	var c, temp, strf;
	strf = "";
	temp = "";
	source = source + "<BR>";
	for (loop = 0; loop < source.length; loop++) {
		c = source.charAt(loop);
		if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_')
		{ temp = temp + c; }
		else
		{
			if (temp.length > 0) {
				if (ubb.keywords.contains(temp)) {
					strf = strf + "[color=blue]" + temp + "[/color]";
				}
				else { strf = strf + temp; }

				temp = "";
			}

			if (c == '/') {
				c = source.charAt(loop + 1);
				if (c == '/') {
					var i;
					i = source.indexOf("<BR>", loop + 2);
					strf = strf + "[color=green]//" + source.substring(loop + 2, i) + "[/color]<BR>";
					loop = i + 3;
				}
				else if (c == '*') {
					var j, h;
					j = source.indexOf("*/", loop + 2);
					h = source.indexOf("<BR>", loop + 2);
					strf = strf + "[color=green]/*";
					if (j == -1) {
						j = source.length;
					}
					if (j > h) {
						strf = strf + source.substring(loop + 2, h) + "[/color]<BR>";
						h = h + 4;
						for (jh = source.indexOf("<BR>", h) ; (jh < j) && (jh != -1) ;) {
							strf = strf + "[color=green]" + source.substring(h, jh) + "[/color]<BR>";
							jh = jh + 4;
							h = jh;
							jh = source.indexOf("<BR>", h);
						}
						strf = strf + "[color=green]" + source.substring(h, j + 2) + "[/color]";
						loop = j + 1;
					}
					else {
						strf = strf + source.substring(loop + 2, j + 2) + "[/color]"
						loop = j + 1;
					}


				}

				else {
					strf = strf + '/';
				}
			}
			else if (c == '\'') {
				var r, o;
				strf = strf + "[color=red]\'";
				r = source.indexOf("\'", loop + 1);
				for (; (source.charAt(r - 1) == '\\') && (r != -1) && (source.charAt(r - 2) != '\\') ;) {
					r = source.indexOf("\'", r + 1);
				}
				o = source.indexOf("<BR>", loop + 1);
				if (r > o || r == -1) {
					r = o - 1;
				}
				strf = strf + source.substring(loop + 1, r + 1) + "[/color]";
				loop = r;
			}
			else if (c == '\"') {
				strf = strf + "[color=hotpink]\"";
				var k, y;
				k = source.indexOf("\"", loop + 1);
				for (; (source.charAt(k - 1) == '\\') && (k != -1) ;) {
					k = source.indexOf("\"", k + 1);
				}
				y = source.indexOf("<BR>", loop + 1);
				if (k > y || k == -1) {
					if (k == -1) {
						k = source.length;
					}
					strf = strf + source.substring(loop + 1, y) + "[/color]<BR>";
					y = y + 4;
					for (ky = source.indexOf("<BR>", y) ; (ky < k) && (ky != -1) ;) {
						strf = strf + "[color=hotpink]" + source.substring(y, ky) + "[/color]<BR>";
						ky = ky + 4;
						y = ky;
						ky = source.indexOf("<BR>", y);
					}
					strf = strf + "[color=hotpink]" + source.substring(y, k + 1) + "[/color]";
					loop = k;

				}

				else {
					strf = strf + source.substring(loop + 1, k) + "\"[/color]";
					loop = k;
				}
			}
			else if (c == '#') {
				var l;
				l = source.indexOf("<BR>", loop + 1);
				strf = strf + "[color=green]" + source.substring(loop, l) + "[/color]<BR>";
				loop = l + 3;
			}

			else strf = strf + c;
		}
	}

	var string2 = strf.split("<BR>");
	var line;
	strf = "<BR>";
	var spacescount = 0;
	for (loop = 0; loop < string2.length - 1; loop++) {
		var beg, end, spaces;
		line = loop + 1;
		if (line < 10) {
			strf = strf + "&nbsp;&nbsp;[color=#008080]" + line + "[/color]&nbsp;&nbsp;&nbsp;&nbsp;";
		}
		if (line > 9 && line < 100) {
			strf = strf + "&nbsp;[color=#008080]" + line + "[/color]&nbsp;&nbsp;&nbsp;&nbsp;";
		}
		if (line > 99) {
			strf = strf + "[color=#008080]" + line + "[/color]&nbsp;&nbsp;&nbsp;&nbsp;";
		}
		spaces = "";
		var flag = false;
		beg = string2[loop].indexOf("{");
		if (beg != -1 && string2[loop].charAt(beg - 1) != '\\') {
			spacescount++;
			flag = true;
		}
		end = string2[loop].indexOf("}")
		if (end != -1 && string2[loop].charAt(end - 1) != '\\')
		{ spacescount--; }

		for (q = 0; q < spacescount; q++) {
			spaces = spaces + "&nbsp;&nbsp;&nbsp;&nbsp;";
		}
		if (flag) {
			spaces = "";
			for (q = 0; q < spacescount - 1; q++) {
				spaces = spaces + "&nbsp;&nbsp;&nbsp;&nbsp;";
			}
		}

		strf = strf + spaces + string2[loop] + "<BR>";
	}
	var fo;
	if (language == "") {
		fo = "Lucida Console";
	}
	else if (language == "=1") {
		fo = "fixedsys";
	}
	else if (language == "=2") {
		fo = "courier new";
	}
	else {
		fo = language.substring(0, language.length);
	}

	source = '<table style="width:100%" cellpadding=5 cellspacing=1 class=tableborder1><tr><TD class="' + tdclass + '" width="100%" onDblClick="copy2cb(ubb.storage.codeSource[\'' + ubb.num['code'].toString() + '\'])"><div style="overflow:auto;"><font face="' + fo + '">' + strf + '</font></div></td></tr></table>';
	return source;
}

var ubb = {
	keywords: ['auto', 'break', 'double', 'else', 'int', 'long', 'struct', 'switch', 'case', 'enum', 'register', 'typedef', 'char', 'extern', 'return', 'union', 'const', 'float', 'short', 'unsigned', 'continue', 'for', 'signed', 'void', 'default', 'goto', 'sizeof', 'volatile', 'do', 'if', 'static', 'while', 'public', 'try', 'default', 'asm', 'delete', 'inline', 'reinterpret_cast', 'typeid', 'typename', 'dynamic_cast', 'mutable', 'bool', 'namespace', 'using', 'break', 'enum', 'new', 'static', 'virtual', 'explicit', 'static_cast', 'catch', 'operator', 'wchar_t', 'class', 'false', 'template', 'while', 'this', 'private', 'throw', 'const_cast', 'friend', 'protected', 'true'],
	num: { 'noubb': 0, 'code': 0, 'img': 0, 'musicAuto': 0 },
	storage: {
		noubb: [],
		code: [],
		codeSource: []
	},
	color: function (str) {
		return str.replace(/\[color=(.[^\[\"\'\\\(\)\:\;]*)\](.*?)\[\/color\]/gi, "<span style=\"color:$1;\">$2</span>");
	},
	i: function (str) {
		return str.replace(/\[i\](.*?)\[\/i\]/gi, "<i>$1</i>");
	},
	u: function (str) {
		return str.replace(/\[u\](.*?)\[\/u\]/gi, "<u>$1</u>");
	},
	b: function (str) {
		return str.replace(/\[b\](.*?)(\[\/b\])/gi, "<b>$1</b>");
	},
	del: function (str) {
		return str.replace(/\[del\](.*?)(\[\/del\])/gi, '<span style="text-decoration:line-through;">$1</span>');
	},
	cursor: function (str) {
		return str.replace(/\[cursor=([A-Za-z]*)\](.*?)(\[\/cursor\])/gi, '<span style="cursor:$1;">$2</span>');
	},
	english: function (str) {
		return str.replace(/\[english\](.*?)\[\/english\]/gi, "<font face=\"Arial\">$1</font>");
	},
	user: function (str) {
		return str.replace(/\[user\](.[^\[]*)\[\/user\]/gi, "<span onclick=\"window.location.href='dispuser.asp?name=$1'\" style=\"cursor:pointer;\">$1</span>");
	},
	pm: function (str) {
		str = str.replace(/\[pm=(.[^\[\'\"\:\(\)\;]*?)\](.*?)\[\/pm\]/gi, "<a href=\"javascript:;\" onclick=\"window.open(\'messanger.asp?action=new&touser=$1\',\'new_win\',\'width=500,height=400,resizable=yes,scrollbars=1\')\">$2</a>");
		str = str.replace(/\[pm\](.[^\[\'\"\:\(\)\;]*?)\[\/pm\]/gi, "<a href=\"javascript:;\" onclick=\"window.open(\'messanger.asp?action=new&touser=$1\',\'new_win\',\'width=500,height=400,resizable=yes,scrollbars=1\')\">点击此处发送论坛短消息给$1</a>");
		return str;
	},
	noubb: function (content) {
		var pattern = /{noubb([0-9]*)}/i;
		while (content.test(pattern)) {
			var tempNum = RegExp.$1;
			content = content.replace(pattern, '<span id="noubb' + tempNum + '" onDblClick="copy2cb(this.innerHTML)">' + ubb.storage.noubb[tempNum] + '</span>');
		}
		return content;
	},
	code: function (str, lang, flag) {
		if (flag == 1) {
			var pattern = /{codes([0-9]*)}/i;
			while (str.test(pattern)) {
				var tempNum = RegExp.$1;
				str = str.replace(pattern, ubb.storage.code[tempNum]);
			}
			return str;
		} else {
			return codeUbb(str, lang);
		}
	},
	media: function (str) {
		var pattern;
		if (canmediacode) {
			//mp3 player
			if (ubb.num['musicAuto'] < 1) {
				pattern = /\[mp3=1\](.[^\[\'\"\(\)]*)\[\/mp3\]/gi;
				if (pattern.test(str)) {
					str = str.replace(/\[mp3=1\](.[^\[\'\"\(\)]*)\[\/mp3\]/i, '<div style="height:20px; width:240px;border:1px #e4e8ef solid;"><embed src="inc/mp3player.swf" width="240" height="20" type="application/x-shockwave-flash" quality="high" flashvars="mp3=$1&autoplay=1&showtime=1"></embed></object></div>');
					ubb.num['musicAuto']++;
				}
			}
			str = str.replace(/\[mp3\](.[^\[\'\"\(\)]*)\[\/mp3\]/gi, '<div style="height:20px; width:240px;border:1px #e4e8ef solid;"><embed src="inc/mp3player.swf" width="240" height="20" type="application/x-shockwave-flash" quality="high" flashvars="mp3=$1&autoplay=0&showtime=1"></embed></object></div>');
			//end mp3 player
			//CC98 Share media
			str = str.replace(/\[MP=*([0-9]*),*([0-9]*),*([01]*)\](http:\/\/share\.cc98\.org\/[0-9A-Za-z]*?)(.file)?\[\/MP]/gi, '<embed type="application/x-mplayer2" pluginspage="http://microsoft.com/windows/mediaplayer/en/download/" src="$4" autoStart="0" width="$1" height="$2" />');
			str = str.replace(/\[RM=*([0-9]*),*([0-9]*),*([01]*)\](http:\/\/share\.cc98\.org\/[0-9A-Za-z]*?)(.file)?\[\/RM]/gi, "<OBJECT classid=clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA class=OBJECT id=RAOCX width=$1 height=$2><PARAM NAME=SRC VALUE=$4><PARAM NAME=CONSOLE VALUE=Clip1><PARAM NAME=CONTROLS VALUE=imagewindow><param name=\"AutoStart\" value=\"False\"></OBJECT><BR><OBJECT classid=CLSID:CFCDAA03-8BE4-11CF-B84B-0020AFBBCCFA height=32 id=video2 width=$1><PARAM NAME=SRC VALUE=$4><PARAM NAME=AUTOSTART VALUE=0><PARAM NAME=CONTROLS VALUE=controlpanel><PARAM NAME=CONSOLE VALUE=Clip1></OBJECT>");
			str = str.replace(/(\[FLASH\])(http:\/\/share\.cc98\.org\/[0-9A-Za-z]*?)(.file)?(\[\/FLASH\])/gi, "<a href=\"$2\" TARGET=_blank><IMG SRC=pic/swf.gif border=0 alt=点击开新窗口欣赏该FLASH动画! height=16 width=16>[全屏欣赏]</a><BR><object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0\" width=\"500\" height=\"400\" align=\"middle\"><param name=\"allowScriptAccess\" value=\"always\" /><param name=\"movie\" value=\"$2\" /><param name=\"quality\" value=\"high\" /><param name=\"wmode\" value=\"transparent\" /><param name=\"devicefont\" value=\"true\" /><embed src=\"$2\" quality=\"high\" wmode=\"transparent\" devicefont=\"true\" width=\"500\" height=\"400\" swLiveConnect=true align=\"middle\" allowScriptAccess=\"always\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" /></object>");
			str = str.replace(/(\[FLASH=*([0-9]*),*([0-9]*),*([01]*)\])(http:\/\/share\.cc98\.org\/[0-9A-Za-z]*?)(.file)?(\[\/FLASH\])/gi, "<a href=\"$5\" TARGET=_blank><IMG SRC=pic/swf.gif border=0 alt=点击开新窗口欣赏该FLASH动画! height=16 width=16>[全屏欣赏]</a><BR><object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0\" width=\"$2\" height=\"$3\" align=\"middle\"><param name=\"allowScriptAccess\" value=\"always\" /><param name=\"movie\" value=\"$5\" /><param name=\"play\" value=\"$4\" /><param name=\"quality\" value=\"high\" /><param name=\"wmode\" value=\"transparent\" /><param name=\"devicefont\" value=\"true\" /><embed src=\"$5\" quality=\"high\" wmode=\"transparent\" devicefont=\"true\" width=\"$2\" height=\"$3\" swLiveConnect=true align=\"middle\" allowScriptAccess=\"always\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" play=\"$4\" /></object>");
			str = str.replace(/(\[FLV\])(http:\/\/share\.cc98\.org\/[0-9A-Za-z]*?)(.file)?(\[\/FLV\])/gi, "<object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0\" id=\"flvplayer\" width=\"400\" height=\"320\" align=\"middle\"><param name=\"allowScriptAccess\" value=\"always\" /><param name=\"movie\" value=\"inc/flvplayer.swf?file=$2\" /><param name=\"quality\" value=\"high\" /><param name=\"wmode\" value=\"transparent\" /><param name=\"devicefont\" value=\"true\" /><param name=\"bgcolor\" value=\"#ffffff\" /><embed src=\"inc/flvplayer.swf?file=$2\" quality=\"high\" wmode=\"transparent\" devicefont=\"true\" bgcolor=\"#ffffff\" width=\"400\" height=\"320\" swLiveConnect=true id=\"flvplayer\" name=\"flvplayer\" align=\"middle\" allowScriptAccess=\"always\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" /></object>");
			str = str.replace(/\[FLV=*([0-9]*),*([0-9]*)\](http:\/\/share\.cc98\.org\/[0-9A-Za-z]*?)(.file)?\[\/FLV\]/gi, "<object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0\" id=\"flvplayer\" width=\"$1\" height=\"$2\" align=\"middle\"><param name=\"allowScriptAccess\" value=\"always\" /><param name=\"movie\" value=\"inc/flvplayer.swf?file=$3\" /><param name=\"quality\" value=\"high\" /><param name=\"wmode\" value=\"transparent\" /><param name=\"devicefont\" value=\"true\" /><param name=\"bgcolor\" value=\"#ffffff\" /><embed src=\"inc/flvplayer.swf?file=$3\" quality=\"high\" wmode=\"transparent\" devicefont=\"true\" bgcolor=\"#ffffff\" width=\"$1\" height=\"$2\" swLiveConnect=true id=\"flvplayer\" name=\"flvplayer\" align=\"middle\" allowScriptAccess=\"always\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" /></object>");
			//End CC98 Share media
			str = str.replace(/\[MP=*([0-9]*),*([0-9]*),*([01]*)\](.[^\[\'\"\(\)]*)\[\/MP]/gi, "<object align=middle classid=CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95 class=OBJECT id=MediaPlayer width=$1 height=$2 ><param name=\"AutoStart\" value=\"False\"><param name=ShowStatusBar value=-1><param name=Filename value=$4><embed type=application/x-oleobject codebase=http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2inf.cab#Version=5,1,52,701 flename=mp src=$4 autoStart=0 width=$1 height=$2></embed></object>");
			str = str.replace(/\[RM=*([0-9]*),*([0-9]*),*([01]*)\](.[^\[\'\"\(\)]*)\[\/RM]/gi, "<OBJECT classid=clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA class=OBJECT id=RAOCX width=$1 height=$2><PARAM NAME=SRC VALUE=$4><PARAM NAME=CONSOLE VALUE=Clip1><PARAM NAME=CONTROLS VALUE=imagewindow><param name=\"AutoStart\" value=\"False\"></OBJECT><BR><OBJECT classid=CLSID:CFCDAA03-8BE4-11CF-B84B-0020AFBBCCFA height=32 id=video2 width=$1><PARAM NAME=SRC VALUE=$4><PARAM NAME=AUTOSTART VALUE=0><PARAM NAME=CONTROLS VALUE=controlpanel><PARAM NAME=CONSOLE VALUE=Clip1></OBJECT>");
			str = str.replace(/(\[FLASH\])(.[^\[\'\"\(\)]*)(\[\/FLASH\])/gi, "<a href=\"$2\" TARGET=_blank><IMG SRC=pic/swf.gif border=0 alt=点击开新窗口欣赏该FLASH动画! height=16 width=16>[全屏欣赏]</a><BR><object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0\" width=\"500\" height=\"400\" align=\"middle\"><param name=\"allowScriptAccess\" value=\"always\" /><param name=\"movie\" value=\"$2\" /><param name=\"quality\" value=\"high\" /><param name=\"wmode\" value=\"transparent\" /><param name=\"devicefont\" value=\"true\" /><embed src=\"$2\" quality=\"high\" wmode=\"transparent\" devicefont=\"true\" width=\"500\" height=\"400\" swLiveConnect=true align=\"middle\" allowScriptAccess=\"never\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" /></object>");
			str = str.replace(/(\[FLASH=*([0-9]*),*([0-9]*),*([01]*)\])(.[^\[\'\"\(\)]*)(\[\/FLASH\])/gi, "<a href=\"$5\" TARGET=_blank><IMG SRC=pic/swf.gif border=0 alt=点击开新窗口欣赏该FLASH动画! height=16 width=16>[全屏欣赏]</a><BR><object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0\" width=\"$2\" height=\"$3\" align=\"middle\"><param name=\"allowScriptAccess\" value=\"always\" /><param name=\"movie\" value=\"$5\" /><param name=\"play\" value=\"$4\" /><param name=\"quality\" value=\"high\" /><param name=\"wmode\" value=\"transparent\" /><param name=\"devicefont\" value=\"true\" /><embed src=\"$5\" quality=\"high\" wmode=\"transparent\" devicefont=\"true\" width=\"$2\" height=\"$3\" swLiveConnect=true align=\"middle\" allowScriptAccess=\"always\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" play=\"$4\" /></object>");
			str = str.replace(/(\[FLV\])(.[^\[\'\"\(\)]*)(\[\/FLV\])/gi, "<object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0\" id=\"flvplayer\" width=\"400\" height=\"320\" align=\"middle\"><param name=\"allowScriptAccess\" value=\"always\" /><param name=\"movie\" value=\"inc/flvplayer.swf?file=$2\" /><param name=\"quality\" value=\"high\" /><param name=\"wmode\" value=\"transparent\" /><param name=\"devicefont\" value=\"true\" /><param name=\"bgcolor\" value=\"#ffffff\" /><embed src=\"inc/flvplayer.swf?file=$2\" quality=\"high\" wmode=\"transparent\" devicefont=\"true\" bgcolor=\"#ffffff\" width=\"400\" height=\"320\" swLiveConnect=true id=\"flvplayer\" name=\"flvplayer\" align=\"middle\" allowScriptAccess=\"always\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" /></object>");
			str = str.replace(/\[FLV=*([0-9]*),*([0-9]*)\](.[^\[\'\"\(\)]*)\[\/FLV\]/gi, "<object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0\" id=\"flvplayer\" width=\"$1\" height=\"$2\" align=\"middle\"><param name=\"allowScriptAccess\" value=\"always\" /><param name=\"movie\" value=\"inc/flvplayer.swf?file=$3\" /><param name=\"quality\" value=\"high\" /><param name=\"wmode\" value=\"transparent\" /><param name=\"devicefont\" value=\"true\" /><param name=\"bgcolor\" value=\"#ffffff\" /><embed src=\"inc/flvplayer.swf?file=$3\" quality=\"high\" wmode=\"transparent\" devicefont=\"true\" bgcolor=\"#ffffff\" width=\"$1\" height=\"$2\" swLiveConnect=true id=\"flvplayer\" name=\"flvplayer\" align=\"middle\" allowScriptAccess=\"always\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" /></object>");
			// Silverlight 支持
			str = ubbSilverlightCode(str);
		} else {
			str = str.replace(/\[MP=*([0-9]*),*([0-9]*)\](.[^\[\'\"\(\)]*)\[\/MP]/gi, "<a href=$3 target=_blank>$3</a>");
			str = str.replace(/\[RM=*([0-9]*),*([0-9]*)\](.[^\[\'\"\(\)]*)\[\/RM]/gi, "<a href=$3 target=_blank>$3</a>");
			str = str.replace(/(\[FLASH\])(.[^\[\'\"\(\)]*)(\[\/FLASH\])/gi, "<IMG SRC=" + icondir + "swf.gif border=0><a href=$2 target=_blank>$2</a>");
			str = str.replace(/(\[FLASH=*([0-9]*),*([0-9]*)\])(.[^\[\'\"\(\)]*)(\[\/FLASH\])/gi, "<IMG SRC=" + icondir + "swf.gif border=0><a href=$4 target=_blank>$4</a>");

			// Silverlight 支持
			str = ubbSilverlightCode(str);

		}
		return str;
	},
	img: function (str, type) {
		var pattern;
		pattern = /\[img\]http:\/\/(www\.cc98\.org|cc\.zju\.edu\.cn)\/(.[^\[]*)\[\/img\]/gi;
		str = str.replace(pattern, '<span style="cursor:pointer;text-decoration:line-through">该文件已在地震中消失了</span>');
		if (type == 0) {
			if (canimgsign) {
				str = str.replace(/\[img\](http:\/\/([a-z]+).cc98.org\/(.[^\[]*))\[\/img\]/ig, '<img src="$1" style="border:0;" onload="if (this.width > 600 || this.height > 300) this.src = \'pic/toobig.jpg\'" />');
				str = str.replace(/\[img\](.[^\[]*)\[\/img\]/ig, '');
			} else {
				pattern = /\[IMG\](http|https|ftp):\/\/(.[^\[\'\"\(\)\:]*)\[\/IMG\]/gi;
				str = str.replace(pattern, "<IMG SRC=\"" + icondir + "gif.gif\" border=0><a onfocus=this.blur() href=\"$1://$2\" target=_blank>$1://$2</a>");
			}
		} else {
			if (canimgcode) {
				//share image
				pattern = /\[IMG=0\](http:\/\/share\.cc98\.org\/[A-Za-z0-9]+)(.file)?\[\/IMG\]/gi;
				str = str.replace(pattern, "<br /><a onfocus=this.blur() href=\"$1\" target=_blank><img src=\"$1\" border=0 alt=\"按此在新窗口浏览图片\" class=\"resizeable\" /></a>");
				pattern = /\[IMG(=1)?\](http:\/\/share\.cc98\.org\/[A-Za-z0-9]+)(.file)?\[\/IMG\]/gi;
				str = str.replace(pattern, '<br /><a onfocus="this.blur();" href="$2" target="_blank" title="按此浏览图片" class="clickloadImage" onclick="this.innerHTML=loadImg(this.href);this.onclick=function(){}; return false;"><img src="' + icondir + 'file.gif" border="0">$2</a>');
				//end share image
				pattern = /\[IMG=0\](.[^\[\'\"\(\)]*)(gif|jpg|jpeg|bmp|png)\[\/IMG\]/gi;
				str = str.replace(pattern, "<br /><a onfocus=this.blur() href=\"$1$2\" target=_blank><img src=\"$1$2\" border=0 alt=\"按此在新窗口浏览图片\" class=\"resizeable\" /></a>");
				pattern = /\[IMG(=1)?\](.[^\[\'\"\(\)]*)(gif|jpg|jpeg|bmp|png)\[\/IMG\]/gi;
				str = str.replace(pattern, '<br /><a onfocus="this.blur();" href="$2$3" target="_blank" title="按此浏览图片" class="clickloadImage" onclick="this.innerHTML=loadImg(this.href);this.onclick=function(){}; return false;"><img src="' + icondir + '$3.gif" border="0">$2$3</a>');
				pattern = /\[UPLOAD=(gif|jpg|jpeg|bmp|png)\](http:\/\/file\.cc98\.org\/.[^\[\'\"\:\(\)]*)(gif|jpg|jpeg|bmp|png)\[\/UPLOAD\]/gi;
				str = str.replace(pattern, "<br /><a href=\"$2$1\" target=\"_blank\"><img src=\"$2$1\" border=0 alt=\"按此在新窗口浏览图片\" class=\"resizeable\"></a>");
				pattern = /\[UPLOAD=(gif|jpg|jpeg|bmp|png),0\](http:\/\/file\.cc98\.org\/.[^\[\'\"\:\(\)]*)(gif|jpg|jpeg|bmp|png)\[\/UPLOAD\]/gi;
				str = str.replace(pattern, "<br /><a href=\"$2$1\" target=\"_blank\"><img src=\"$2$1\" border=0 alt=\"按此在新窗口浏览图片\" class=\"resizeable\"></a>");
				pattern = /\[UPLOAD=(gif|jpg|jpeg|bmp|png),1\](http:\/\/file\.cc98\.org\/.[^\[\'\"\:\(\)]*)(gif|jpg|jpeg|bmp|png)\[\/UPLOAD\]/gi;
				str = str.replace(pattern, '<br /><a onfocus="this.blur();" href="$2$1" target="_blank" title="按此浏览图片" class="clickloadImage" onclick="this.innerHTML=loadImg(this.href);this.onclick=function(){}; return false;"><img src="' + icondir + '$1.gif" border="0">$2$1</a>');
			} else {
				pattern = /\[IMG([=]*)([01]*)\](http|https|ftp):\/\/(.[^\[\'\"\:\(\)]*)\[\/IMG\]/gi;
				str = str.replace(pattern, '<br><a onfocus="this.blur();" href="$3://$4" target="_blank" onclick="this.innerHTML=loadImg(this.href);this.onclick=function(){}; return false;"><img src="' + icondir + 'gif.gif" border="0">$3://$4</a>');
				pattern = /\[UPLOAD=(gif|jpg|jpeg|bmp|png)([,]*)([01]*)\](http:\/\/file\.cc98\.org\/.[^\[\'\"\:\(\)]*)(gif|jpg|jpeg|bmp|png)\[\/UPLOAD\]/gi;
				str = str.replace(pattern, '<br><a href="$4$5" target="_blank" class="clickloadImage"  onclick="this.innerHTML=loadImg(this.href);this.onclick=function(){}; return false;"><img src="' + icondir + '$5.gif" border=0>$4$5</a>');
			}
		}
		return str;
	},
	file: function (str) {
		//pattern=/\[UPLOAD=txt([,]*)([01]*)\]http:\/\/file\.cc98\.org\/(uploadfile\/.[^\[\'\"\:\(\)]*)\[\/UPLOAD\]/gi;
		//str = str.replace(pattern,'<img src="'+icondir+'txt.gif" border="0" /><a href="filedown.asp?url=$3" target="_self">点击下载文件</a>');
		pattern = /\[UPLOAD=(.[^\[\'\"\:\(\)]*?)([,]*)([01]*)\](http:\/\/file\.cc98\.org\/.[^\[\'\"\:\(\)]*)\[\/UPLOAD\]/gi;
		str = str.replace(pattern, "<BR><IMG SRC=\"" + icondir + "$1.gif\" border=0> <a href=\"$4\">点击浏览该文件</a>");
		pattern = /\[UPLOAD=(.[^\[\'\"\:\(\)]*)\](.[^\[\'\"\:\(\)]*)\[\/UPLOAD\]/gi;
		str = str.replace(pattern, '<span style="cursor:pointer;text-decoration:line-through">该文件已在地震中消失了</span>');
		return str;
	},
	quote: function (str) {
		str = str.replace(/\[QUOTE=1\](.*?)\[\/QUOTE\]/gi, "<div style=\"width:100%\" class=\"" + tdclass + "\"><div class=\"quoteMaxHeightDiv\" style=\"overflow:auto; padding:5px 5px 5px 5px;\">$1</div></div>");
		str = str.replace(/\[QUOTE=0\](.*?)\[\/QUOTE\]/gi, "<div style=\"width:100%\" class=\"" + tdclass + "\"><div style=\"overflow:auto; padding:5px 5px 5px 5px;\">$1</div></div>");
		str = str.replace(/\[QUOTE\](.*?)\[\/QUOTE\]/gi, "<div style=\"width:100%\" class=\"" + tdclass + "\"><div style=\"overflow:auto; padding:5px 5px 5px 5px;\">$1</div></div>");
		str = str.replace(/\[QUOTEX\](.*?)<BR>(.*?)\[\/QUOTEX\]/gi, "<div style=\"width:100%\" class=\"" + tdclass + "\"><div style=\"padding-left:5px; line-height:21px;\"><b>$1</b></div><div class=\"quoteMaxHeightDiv\" style=\"overflow:auto; padding-left:5px;\">$2</div></div>");
		return str;
	},
	table: function (str) {

		// 替换函数
		var replaceTable = function (all, content) {

			return String.format("<table style=\"width: 100%;\" cellpadding=\"5\" cellspacing=\"1\" class=\"tableborder1\">{0}</table>", content);
		}

		return str.replace(/\[table](.*?)\[\/table]/gi, replaceTable);
	},

	tr: function (str) {

		// 替换函数
		var replaceTR = function (all, content) {

			return String.format("<tr>{0}</tr>", content);
		}

		return str.replace(/\[tr](.*?)\[\/tr]/gi, replaceTR);
	},

	th: function (str) {
		// 替换函数
		var replaceTH = function (all, rowSpan, colSpan, content) {
			var str = "<th";

			if (colSpan != undefined) {
				str = str + String.format(" colspan=\"{0}\"", colSpan);
			}

			if (rowSpan != undefined) {
				str = str + String.format(" rowspan=\"{0}\"", rowSpan);
			}

			str = str + String.format(">{0}</th>", content);

			return str;
		}

		return str.replace(/\[th(?:\=(\d+),(\d+))?](.*?)\[\/th]/gi, replaceTH);
	},


	td: function (str) {

		// 替换函数
		var replaceTD = function (all, rowSpan, colSpan, content) {

			var str = "<td";

			if (colSpan != undefined) {
				str = str + String.format(" colspan=\"{0}\"", colSpan);
			}

			if (rowSpan != undefined) {
				str = str + String.format(" rowspan=\"{0}\"", rowSpan);
			}

			str = str + String.format(" class=\"tablebody1\">{0}</td>", content);

			return str;
		}

		return str.replace(/\[td(?:\=(\d+),(\d+))?](.*?)\[\/td]/gi, replaceTD);
	},

	url: function (str, signatureMode) {

		var pattern;

		if (!signatureMode) {
			str = str.replace(/\[url,t=(blank|self|parent)\](.[^\[]*)\[\/url\]/gi, "<a href=\"$2\" target=\"_$1\">$2</a>");
			str = str.replace(/\[url=(.[^\[\'\"\(\)]*?),t=(blank|self|parent)\](.[^\[]*)\[\/url\]/gi, "<a href=\"$1\" target=\"_$2\">$3</a>");
			//pattern=/\[url\](http:\/\/file\.cc98\.org\/(uploadfile\/.[^\[\'\"\:\(\)]*)\.txt)\[\/url\]/gi;
			//str = str.replace(pattern,'<a href="filedown.asp?url=$2.txt" target="_blank">$1</a>');
			//pattern=/\[url\](http:\/\/www\.cc98\.org\/(uploadfile\/.[^\[\'\"\:\(\)]*)\.txt)\[\/url\]/gi;
			//str = str.replace(pattern,'<a href="filedown.asp?url=$2.txt" target="_blank">$1</a>');
			//pattern=/\[url\](http:\/\/10\.10\.98\.98\/(uploadfile\/.[^\[\'\"\:\(\)]*)\.txt)\[\/url\]/gi;
			//str = str.replace(pattern,'<a href="filedown.asp?url=$2.txt" target="_blank">$1</a>');

			str = str.replace(/\[url\]http\:\/\/(www\.cc98\.org|10\.10\.98\.98|cc\.zju\.edu\.cn)\/(.[^\[\'\"\:\(\)]*)\[\/url\]/gi, "<a href=\"$2\" target=\"_blank\">$2</a>");
			str = str.replace(/\[url=http\:\/\/(www\.cc98\.org|10\.10\.98\.98|cc\.zju\.edu\.cn)\/(.[^\[\'\"\:\(\)]*)\](.*?)\[\/url\]/gi, "<a href=\"$2\" target=\"_blank\">$3</a>");

			//pattern=/\[url=(http:\/\/file\.cc98\.org\/(uploadfile\/.[^\[\'\"\:\(\)]*)\.txt)\](.*?)\[\/url\]/gi;
			//str = str.replace(pattern,'<a href="filedown.asp?url=$2.txt" target="_self">$3</a>');
			//pattern=/\[url=(http:\/\/www\.cc98\.org\/(uploadfile\/.[^\[\'\"\:\(\)]*)\.txt)\](.*?)\[\/url\]/gi;
			//str = str.replace(pattern,'<a href="filedown.asp?url=$2.txt" target="_self">$3</a>');
			//pattern=/\[url=(http:\/\/10\.10\.98\.98\/(uploadfile\/.[^\[\'\"\:\(\)]*)\.txt)\](.*?)\[\/url\]/gi;
			//str = str.replace(pattern,'<a href="filedown.asp?url=$2.txt" target="_self">$4</a>');

			str = str.replace(/\[url\](.[^\[]*)\[\/url\]/gi, "<a href=\"$1\" target=\"_blank\">$1</a>");
			str = str.replace(/\[url=(.[^\[\'\"\(\)]*)\](.*?)\[\/url\]/gi, "<a href=\"$1\" target=\"_blank\">$2</a>");
			str = str.replace(/\[durl=(.[^\[\'\"\(\)]*?),(.[^\[\'\"\(\)]*?)\](.*?)\[\/durl\]/gi, "<img align=absmiddle src=pic/url.gif border=0><a href=\"javascript:;\" onclick=\"window.open(\'$1\');window.open(\'$2\');\" >$3</a>");
			//自动识别网址
			//pattern = /http:\/\/(www\.cc98\.org|10\.10\.98\.98|cc\.zju\.edu\.cn)\/([A-Za-z0-9\.\/=\?%\-&_~`@\':+!;#]+)/gi;
			//str = str.replace(pattern, '<a target="_blank" href="$2">$2</a> &nbsp;');
			pattern = /^((http|https|ftp|rtsp|mms):(\/\/|\\\\)[A-Za-z0-9\.\/=\?%\-&_~`@\':+!;#]+)/gi;
			str = str.replace(pattern, "<img align=absmiddle src=pic/url.gif border=0><a target=_blank href=$1>$1</a>");
			pattern = /((http|https|ftp|rtsp|mms):(\/\/|\\\\)[A-Za-z0-9\.\/=\?%\-&_~`@\':+!;#]+)<BR>/gi;
			str = str.replace(pattern, "<img align=absmiddle src=pic/url.gif border=0><a target=_blank href=$1>$1</a><BR>");
			pattern = /([^>=\"]|<BR>)((http|https|ftp|rtsp|mms):(\/\/|\\\\)[A-Za-z0-9\.\/=\?%\-&_~`@\':+!;#]+)/gi;
			str = str.replace(pattern, "$1<img align=absmiddle src=pic/url.gif border=0><a target=_blank href=$2>$2</a>");
		} else {
			//pattern=/\[url\](http:\/\/file\.cc98\.org\/(uploadfile\/.[^\[\'\"\:\(\)]*)\.txt)\[\/url\]/gi;
			//str = str.replace(pattern,'<a href="filedown.asp?url=$2.txt" target="_blank">$1</a>');
			//pattern=/\[url\](http:\/\/www\.cc98\.org\/(uploadfile\/.[^\[\'\"\:\(\)]*)\.txt)\[\/url\]/gi;
			//str = str.replace(pattern,'<a href="filedown.asp?url=$2.txt" target="_blank">$1</a>');
			//pattern=/\[url\](http:\/\/10\.10\.98\.98\/(uploadfile\/.[^\[\'\"\:\(\)]*)\.txt)\[\/url\]/gi;
			//str = str.replace(pattern,'<a href="filedown.asp?url=$2.txt" target="_blank">$1</a>');

			str = str.replace(/\[url\](http\:\/\/((([a-z]+)\.cc98\.org)|(([a-z\.]+)\.zju\.edu\.cn))\/(.[^\[\'\"\:\(\)]*))\[\/url\]/ig, '<a href="$1" target="_blank">$1</a>');
			str = str.replace(/\[url=(http\:\/\/((([a-z]+)\.cc98\.org)|(([a-z\.]+)\.zju\.edu\.cn))\/(.[^\[\'\"\:\(\)]*))\](.*?)\[\/url\]/ig, '<a href="$1" target="_blank">$8</a>');

			// str = str.replace(/\[url\](([a-z0-9\.\/])+((\/)?(.[^\[\'\"\:\(\)]*))?)\[\/url\]/ig, '<a href="$1" target="_blank">$1</a>');
			// str = str.replace(/\[url=(([a-z0-9\.\/])+((\/)?(.[^\[\'\"\:\(\)]*))?)\](.*?)\[\/url\]/ig, '<a href="$1" target="_blank">$6</a>');

			str = str.replace(/\[url\](.[^\[]*)\[\/url\]/ig, '');
			str = str.replace(/\[url=(.[^\[\'\"\(\)]*)\](.*?)\[\/url\]/ig, '');

			//pattern=/\[url=(http:\/\/file\.cc98\.org\/(uploadfile\/.[^\[\'\"\:\(\)]*)\.txt)\](.*?)\[\/url\]/gi;
			//str = str.replace(pattern,'<a href="filedown.asp?url=$2.txt" target="_self">$3</a>');
			//pattern=/\[url=(http:\/\/www\.cc98\.org\/(uploadfile\/.[^\[\'\"\:\(\)]*)\.txt)\](.*?)\[\/url\]/gi;
			//str = str.replace(pattern,'<a href="filedown.asp?url=$2.txt" target="_self">$3</a>');
			//pattern=/\[url=(http:\/\/10\.10\.98\.98\/(uploadfile\/.[^\[\'\"\:\(\)]*)\.txt)\](.*?)\[\/url\]/gi;
			//str = str.replace(pattern,'<a href="filedown.asp?url=$2.txt" target="_self">$4</a>');



			//自动识别网址
			//pattern = /http:\/\/(www\.cc98\.org|10\.10\.98\.98|cc\.zju\.edu\.cn)\/([A-Za-z0-9\.\/=\?%\-&_~`@\':+!;#]+)/gi;
			//str = str.replace(pattern, '<a target="_blank" href="$2">$2</a> &nbsp;');
			pattern = /^((http|https|ftp|rtsp|mms):(\/\/|\\\\)((([a-z]+)\.cc98\.org)|(([a-z\.]+)\.zju\.edu\.cn))\/[A-Za-z0-9\.\/=\?%\-&_~`@\':+!;#]+)/ig;
			str = str.replace(pattern, '<img align="absmiddle" src="pic/url.gif" style="border:0;" /><a href="$1" target="_blank">$1</a>');
			pattern = /((http|https|ftp|rtsp|mms):(\/\/|\\\\)((([a-z]+)\.cc98\.org)|(([a-z\.]+)\.zju\.edu\.cn))\/[A-Za-z0-9\.\/=\?%\-&_~`@\':+!;#]+)<BR>/ig;
			str = str.replace(pattern, '<img align="absmiddle" src="pic/url.gif" style="border:0;" /><a href="$1" target="_blank">$1</a><br />');
			pattern = /([^>=\"]|<BR>)((http|https|ftp|rtsp|mms):(\/\/|\\\\)((([a-z]+)\.cc98\.org)|(([a-z\.]+)\.zju\.edu\.cn))\/[A-Za-z0-9\.\/=\?%\-&_~`@\':+!;#]+)/ig;
			str = str.replace(pattern, '$1<img align="absmiddle" src="pic/url.gif" style="border:0;" /><a href="$2" target="_blank">$2</a>');

			pattern = /^((http|https|ftp|rtsp|mms):(\/\/|\\\\)[A-Za-z0-9\.\/=\?%\-&_~`@\':+!;#]+)/gi;
			str = str.replace(pattern, '');
			pattern = /((http|https|ftp|rtsp|mms):(\/\/|\\\\)[A-Za-z0-9\.\/=\?%\-&_~`@\':+!;#]+)<BR>/gi;
			str = str.replace(pattern, '<br />');
			pattern = /([^>=\"]|<BR>)((http|https|ftp|rtsp|mms):(\/\/|\\\\)[A-Za-z0-9\.\/=\?%\-&_~`@\':+!;#]+)/gi;
			str = str.replace(pattern, '$1');
		}
		return str;
	},
	emotion: function (str) {
		if (canemcode) {
			pattern = /\[em([0-9]+)\]/gi;
			str = str.replace(pattern, "<img src=\"" + emotdir + "emot$1.gif\" border=0 align=middle>");
		} else {
			pattern = /\[em(.[^\[\'\"\:\(\)\;]*)\]/gi;
			str = str.replace(pattern, "");
		}
		return str;
	},
	share: function (str) {
		str = str.replace(/\[share=([A-Za-z0-9]*)\](.*?)\[\/share\]/gi, '<img src="' + icondir + 'file.gif" border="0" /><a href="http://share.cc98.org/$1" target="_blank">点此下载 $2</a>');
		str = str.replace(/\[share\]([A-Za-z0-9]*)\[\/share\]/gi, '<img src="' + icondir + 'file.gif" border="0" /><a href="http://share.cc98.org/$1" target="_blank">点此下载 $1 文件</a>');
		return str;
	},
	topic: function (str) {
		str = str.replace(/\[topic=(\d+),(\d+)\](.*?)\[\/topic\]/gi, "<a href=\"/dispbbs.asp?boardid=$1&id=$2\">$3</a>");
		return str;
	},
	board: function (str) {
		str = str.replace(/\[board=(\d+)](.*?)\[\/board\]/gi, "<a href=\"/list.asp?boardid=$1\">$2</a>");
		return str;
	},
	filter: function (str) {
		str = str.replace(/\[SHADOW=*([0-9]*),*(#*[a-z0-9]*),*([0-9]*)\](.*?)\[\/SHADOW]/gi, "<table width=$1 ><tr><td style=\"filter:shadow(color=$2, strength=$3)\">$4</td></tr></table>");
		str = str.replace(/\[GLOW=*([0-9]*),*(#*[a-z0-9]*),*([0-9]*)\](.*?)\[\/GLOW]/gi, "<table width=$1 ><tr><td style=\"filter:glow(color=$2, strength=$3)\">$4</td></tr></table>");
		return str;
	},
	subscription: function (str) {
		return str.replace(/\[subscription=(.[^\[\'\"\:\(\)\;]*?)\](.*?)\[\/subscription\]/gi, "<a href=\"javascript:;\" onclick=\"window.open(\'subscription.asp?action=adduser&topicid=$1\',\'new_win\',\'width=500,height=400,resizable=yes,scrollbars=1\')\">$2</a>");
	},
	nothot: function (str) {
		return str.replace(/\[nothot\](.[^\[]*)\[\/nothot\]/gi, "<span name=\"nothot\">$1</span>");
	},
	font: function (str) {
		return str.replace(/\[font=(.[^\[\'\"\:\(\)\;]*?)\](.*?)\[\/font\]/gi, '<span style="font-family:$1;">$2</span>');
	},
	list: function (str) {
		str = str.replace(/\[\*\]/gi, "<li>");
		str = str.replace(/\[list\](.*?)\[\/list\]/gi, "<ul>$1</ul>");
		str = str.replace(/\[list=(1|a|A)\](.*?)\[\/list\]/gi, "<ul type=\"$1\">$2</ul>");
		return str;
	},
	align: function (str) {
		str = str.replace(/\[float=(left|right)\](.*?)\[\/float\]/gi, "<br style=\"clear: both\"><span style=\"float:$1;\">$2</span>");
		str = str.replace(/\[align=(center|left|right)\](.*?)\[\/align\]/gi, "<div align=\"$1\">$2</div>");
		str = str.replace(/\[right\](.*?)(\[\/right\])/gi, "<div align=\"right\">$1</div>");
		str = str.replace(/\[left\](.*?)(\[\/left\])/gi, "<div align=\"left\">$1</div>");
		str = str.replace(/\[center\](.*?)(\[\/center\])/gi, "<div align=\"center\">$1</div>");
		return str;
	},
	size: function (str) {
		var pattern;
		pattern = /\[size=([0-9]*)(pt|px)*\](.*?)\[\/size\]/i;
		while (pattern.test(str)) {
			var unit = RegExp.$2;
			var size = RegExp.$1;
			if (unit == '') {
				size = size > 7 ? 35 : size * 5;
				str = str.replace(pattern, '<span style="font-size:' + size.toString() + 'px; line-height:110%">$3</span>');
			} else {
				size = size > 35 ? 35 : size;
				str = str.replace(pattern, '<span style="font-size:' + size.toString() + unit.toString() + '; line-height:110%">$3</span>');
			}
		}
		return str;
	},
	noEdit: function (str) {
		if (tdclass == 'tablebody1')
			return str.replace(/\[noedit\]/gi, '[color=#e4e8ef]')
		else
			return str.replace(/\[noedit\]/gi, '[color=#fff]')
	},
	software: function (str) {
		str = str.replace(/\[Software\]\[\/Software\]/ig, '<a href="http://software.cc98.org/" target="_blank"><img src="http://file.cc98.org/uploadfile/2010/10/7/2239942335.jpg" border="0" /></a>');
		return str;
	}
}