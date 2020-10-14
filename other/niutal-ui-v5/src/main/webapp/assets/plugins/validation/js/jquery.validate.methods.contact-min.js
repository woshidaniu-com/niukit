/*
 * 官方扩展校验方法：详情参见：jquery.validate.additional.js
 */
(function(a) {
	function b(c) {
		return c.replace(/<.[^<>]*?>/g, " ").replace(/&nbsp;|&#160;/gi, " ").replace(/[.(),;:!?%#$'\"_+=\/\-\u201c\u201d\u2019]*/g, "")
	}
	a.validator.addMethod("maxWords", function(d, c, e) {
		return this.optional(c) || b(d).match(/\b\w+\b/g).length <= e
	});
	a.validator.addMethod("minWords", function(d, c, e) {
		return this.optional(c) || b(d).match(/\b\w+\b/g).length >= e
	});
	a.validator.addMethod("rangeWords", function(f, c, g) {
		var e = b(f),
			d = /\b\w+\b/g;
		return this.optional(c) || e.match(d).length >= g[0] && e.match(d).length <= g[1]
	});
	a.validator.addMethod("accept", function(g, e, j) {
		var h = typeof j === "string" ? j.replace(/\s/g, "").replace(/,/g, "|") : "image/*",
			f = this.optional(e),
			d, c;
		if(f) {
			return f
		}
		if(a(e).attr("type") === "file") {
			h = h.replace(/\*/g, ".*");
			if(e.files && e.files.length) {
				for(d = 0; d < e.files.length; d++) {
					c = e.files[d];
					if(!c.type.match(new RegExp(".?(" + h + ")$", "i"))) {
						return false
					}
				}
			}
		}
		return true
	});
	a.validator.addMethod("alphanumeric", function(d, c) {
		return this.optional(c) || /^\w+$/i.test(d)
	});
	a.validator.addMethod("bankaccountNL", function(h, e) {
		if(this.optional(e)) {
			return true
		}
		if(!(/^[0-9]{9}|([0-9]{2} ){3}[0-9]{3}$/.test(h))) {
			return false
		}
		var g = h.replace(/ /g, ""),
			f = 0,
			c = g.length,
			j, d, i;
		for(j = 0; j < c; j++) {
			d = c - j;
			i = g.substring(j, j + 1);
			f = f + d * i
		}
		return f % 11 === 0
	});
	a.validator.addMethod("bankorgiroaccountNL", function(d, c) {
		return this.optional(c) || (a.validator.methods.bankaccountNL.call(this, d, c)) || (a.validator.methods.giroaccountNL.call(this, d, c))
	});
	a.validator.addMethod("bic", function(d, c) {
		return this.optional(c) || /^([A-Z]{6}[A-Z2-9][A-NP-Z1-2])(X{3}|[A-WY-Z0-9][A-Z0-9]{2})?$/.test(d)
	});
	a.validator.addMethod("cifES", function(k) {
		var e = [],
			c, h, g, j, f, d;
		k = k.toUpperCase();
		if(!k.match("((^[A-Z]{1}[0-9]{7}[A-Z0-9]{1}$|^[T]{1}[A-Z0-9]{8}$)|^[0-9]{8}[A-Z]{1}$)")) {
			return false
		}
		for(g = 0; g < 9; g++) {
			e[g] = parseInt(k.charAt(g), 10)
		}
		h = e[2] + e[4] + e[6];
		for(j = 1; j < 8; j += 2) {
			f = (2 * e[j]).toString();
			d = f.charAt(1);
			h += parseInt(f.charAt(0), 10) + (d === "" ? 0 : parseInt(d, 10))
		}
		if(/^[ABCDEFGHJNPQRSUVW]{1}/.test(k)) {
			h += "";
			c = 10 - parseInt(h.charAt(h.length - 1), 10);
			k += c;
			return(e[8].toString() === String.fromCharCode(64 + c) || e[8].toString() === k.charAt(k.length - 1))
		}
		return false
	});
	a.validator.addMethod("creditcardtypes", function(d, c, e) {
		if(/[^0-9\-]+/.test(d)) {
			return false
		}
		d = d.replace(/\D/g, "");
		var f = 0;
		if(e.mastercard) {
			f |= 1
		}
		if(e.visa) {
			f |= 2
		}
		if(e.amex) {
			f |= 4
		}
		if(e.dinersclub) {
			f |= 8
		}
		if(e.enroute) {
			f |= 16
		}
		if(e.discover) {
			f |= 32
		}
		if(e.jcb) {
			f |= 64
		}
		if(e.unknown) {
			f |= 128
		}
		if(e.all) {
			f = 1 | 2 | 4 | 8 | 16 | 32 | 64 | 128
		}
		if(f & 1 && /^(5[12345])/.test(d)) {
			return d.length === 16
		}
		if(f & 2 && /^(4)/.test(d)) {
			return d.length === 16
		}
		if(f & 4 && /^(3[47])/.test(d)) {
			return d.length === 15
		}
		if(f & 8 && /^(3(0[012345]|[68]))/.test(d)) {
			return d.length === 14
		}
		if(f & 16 && /^(2(014|149))/.test(d)) {
			return d.length === 15
		}
		if(f & 32 && /^(6011)/.test(d)) {
			return d.length === 16
		}
		if(f & 64 && /^(3)/.test(d)) {
			return d.length === 16
		}
		if(f & 64 && /^(2131|1800)/.test(d)) {
			return d.length === 15
		}
		if(f & 128) {
			return true
		}
		return false
	});
	a.validator.addMethod("currency", function(h, e, i) {
		var d = typeof i === "string",
			g = d ? i : i[0],
			c = d ? true : i[1],
			f;
		g = g.replace(/,/g, "");
		g = c ? g + "]" : g + "]?";
		f = "^[" + g + "([1-9]{1}[0-9]{0,2}(\\,[0-9]{3})*(\\.[0-9]{0,2})?|[1-9]{1}[0-9]{0,}(\\.[0-9]{0,2})?|0(\\.[0-9]{0,2})?|(\\.[0-9]{1,2})?)$";
		f = new RegExp(f);
		return this.optional(e) || f.test(h)
	});
	a.validator.addMethod("dateITA", function(g, e) {
		var c = false,
			i = /^\d{1,2}\/\d{1,2}\/\d{4}$/,
			k, f, d, h, j;
		if(i.test(g)) {
			k = g.split("/");
			f = parseInt(k[0], 10);
			d = parseInt(k[1], 10);
			h = parseInt(k[2], 10);
			j = new Date(h, d - 1, f, 12, 0, 0, 0);
			if((j.getFullYear() === h) && (j.getMonth() === d - 1) && (j.getDate() === f)) {
				c = true
			} else {
				c = false
			}
		} else {
			c = false
		}
		return this.optional(e) || c
	});
	a.validator.addMethod("dateNL", function(d, c) {
		return this.optional(c) || /^(0?[1-9]|[12]\d|3[01])[\.\/\-](0?[1-9]|1[012])[\.\/\-]([12]\d)?(\d\d)$/.test(d)
	});
	a.validator.addMethod("extension", function(d, c, e) {
		e = typeof e === "string" ? e.replace(/,/g, "|") : "png|jpe?g|gif";
		return this.optional(c) || d.match(new RegExp(".(" + e + ")$", "i"))
	});
	a.validator.addMethod("giroaccountNL", function(d, c) {
		return this.optional(c) || /^[0-9]{1,7}$/.test(d)
	});
	a.validator.addMethod("iban", function(r, m) {
		if(this.optional(m)) {
			return true
		}
		var h = r.replace(/ /g, "").toUpperCase(),
			j = "",
			n = true,
			t = "",
			s = "",
			e, g, f, q, o, c, l, k, d;
		if(!(/^([a-zA-Z0-9]{4} ){2,8}[a-zA-Z0-9]{1,4}|[a-zA-Z0-9]{12,34}$/.test(h))) {
			return false
		}
		e = h.substring(0, 2);
		c = {
			AL: "\\d{8}[\\dA-Z]{16}",
			AD: "\\d{8}[\\dA-Z]{12}",
			AT: "\\d{16}",
			AZ: "[\\dA-Z]{4}\\d{20}",
			BE: "\\d{12}",
			BH: "[A-Z]{4}[\\dA-Z]{14}",
			BA: "\\d{16}",
			BR: "\\d{23}[A-Z][\\dA-Z]",
			BG: "[A-Z]{4}\\d{6}[\\dA-Z]{8}",
			CR: "\\d{17}",
			HR: "\\d{17}",
			CY: "\\d{8}[\\dA-Z]{16}",
			CZ: "\\d{20}",
			DK: "\\d{14}",
			DO: "[A-Z]{4}\\d{20}",
			EE: "\\d{16}",
			FO: "\\d{14}",
			FI: "\\d{14}",
			FR: "\\d{10}[\\dA-Z]{11}\\d{2}",
			GE: "[\\dA-Z]{2}\\d{16}",
			DE: "\\d{18}",
			GI: "[A-Z]{4}[\\dA-Z]{15}",
			GR: "\\d{7}[\\dA-Z]{16}",
			GL: "\\d{14}",
			GT: "[\\dA-Z]{4}[\\dA-Z]{20}",
			HU: "\\d{24}",
			IS: "\\d{22}",
			IE: "[\\dA-Z]{4}\\d{14}",
			IL: "\\d{19}",
			IT: "[A-Z]\\d{10}[\\dA-Z]{12}",
			KZ: "\\d{3}[\\dA-Z]{13}",
			KW: "[A-Z]{4}[\\dA-Z]{22}",
			LV: "[A-Z]{4}[\\dA-Z]{13}",
			LB: "\\d{4}[\\dA-Z]{20}",
			LI: "\\d{5}[\\dA-Z]{12}",
			LT: "\\d{16}",
			LU: "\\d{3}[\\dA-Z]{13}",
			MK: "\\d{3}[\\dA-Z]{10}\\d{2}",
			MT: "[A-Z]{4}\\d{5}[\\dA-Z]{18}",
			MR: "\\d{23}",
			MU: "[A-Z]{4}\\d{19}[A-Z]{3}",
			MC: "\\d{10}[\\dA-Z]{11}\\d{2}",
			MD: "[\\dA-Z]{2}\\d{18}",
			ME: "\\d{18}",
			NL: "[A-Z]{4}\\d{10}",
			NO: "\\d{11}",
			PK: "[\\dA-Z]{4}\\d{16}",
			PS: "[\\dA-Z]{4}\\d{21}",
			PL: "\\d{24}",
			PT: "\\d{21}",
			RO: "[A-Z]{4}[\\dA-Z]{16}",
			SM: "[A-Z]\\d{10}[\\dA-Z]{12}",
			SA: "\\d{2}[\\dA-Z]{18}",
			RS: "\\d{18}",
			SK: "\\d{20}",
			SI: "\\d{15}",
			ES: "\\d{20}",
			SE: "\\d{20}",
			CH: "\\d{5}[\\dA-Z]{12}",
			TN: "\\d{20}",
			TR: "\\d{5}[\\dA-Z]{17}",
			AE: "\\d{3}\\d{16}",
			GB: "[A-Z]{4}\\d{14}",
			VG: "[\\dA-Z]{4}\\d{16}"
		};
		o = c[e];
		if(typeof o !== "undefined") {
			l = new RegExp("^[A-Z]{2}\\d{2}" + o + "$", "");
			if(!(l.test(h))) {
				return false
			}
		}
		g = h.substring(4, h.length) + h.substring(0, 4);
		for(k = 0; k < g.length; k++) {
			f = g.charAt(k);
			if(f !== "0") {
				n = false
			}
			if(!n) {
				j += "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(f)
			}
		}
		for(d = 0; d < j.length; d++) {
			q = j.charAt(d);
			s = "" + t + "" + q;
			t = s % 97
		}
		return t === 1
	});
	a.validator.addMethod("integer", function(d, c) {
		return this.optional(c) || /^-?\d+$/.test(d)
	});
	a.validator.addMethod("ipv4", function(d, c) {
		return this.optional(c) || /^(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)$/i.test(d)
	});
	a.validator.addMethod("ipv6", function(d, c) {
		return this.optional(c) || /^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|(([0-9A-Fa-f]{1,4}:){0,5}:((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|(::([0-9A-Fa-f]{1,4}:){0,5}((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|([0-9A-Fa-f]{1,4}::([0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})|(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:))$/i.test(d)
	});
	a.validator.addMethod("lettersonly", function(d, c) {
		return this.optional(c) || /^[a-z]+$/i.test(d)
	});
	a.validator.addMethod("letterswithbasicpunc", function(d, c) {
		return this.optional(c) || /^[a-z\-.,()'"\s]+$/i.test(d)
	});
	a.validator.addMethod("mobileNL", function(d, c) {
		return this.optional(c) || /^((\+|00(\s|\s?\-\s?)?)31(\s|\s?\-\s?)?(\(0\)[\-\s]?)?|0)6((\s|\s?\-\s?)?[0-9]){8}$/.test(d)
	});
	a.validator.addMethod("mobileUK", function(c, d) {
		c = c.replace(/\(|\)|\s+|-/g, "");
		return this.optional(d) || c.length > 9 && c.match(/^(?:(?:(?:00\s?|\+)44\s?|0)7(?:[1345789]\d{2}|624)\s?\d{3}\s?\d{3})$/)
	});
	a.validator.addMethod("nieES", function(c) {
		c = c.toUpperCase();
		if(!c.match("((^[A-Z]{1}[0-9]{7}[A-Z0-9]{1}$|^[T]{1}[A-Z0-9]{8}$)|^[0-9]{8}[A-Z]{1}$)")) {
			return false
		}
		if(/^[T]{1}/.test(c)) {
			return(c[8] === /^[T]{1}[A-Z0-9]{8}$/.test(c))
		}
		if(/^[XYZ]{1}/.test(c)) {
			return(c[8] === "TRWAGMYFPDXBNJZSQVHLCKE".charAt(c.replace("X", "0").replace("Y", "1").replace("Z", "2").substring(0, 8) % 23))
		}
		return false
	});
	a.validator.addMethod("nifES", function(c) {
		c = c.toUpperCase();
		if(!c.match("((^[A-Z]{1}[0-9]{7}[A-Z0-9]{1}$|^[T]{1}[A-Z0-9]{8}$)|^[0-9]{8}[A-Z]{1}$)")) {
			return false
		}
		if(/^[0-9]{8}[A-Z]{1}$/.test(c)) {
			return("TRWAGMYFPDXBNJZSQVHLCKE".charAt(c.substring(8, 0) % 23) === c.charAt(8))
		}
		if(/^[KLM]{1}/.test(c)) {
			return(c[8] === String.fromCharCode(64))
		}
		return false
	});
	a.validator.addMethod("nowhitespace", function(d, c) {
		return this.optional(c) || /^\S+$/i.test(d)
	});
	a.validator.addMethod("pattern", function(d, c, e) {
		if(this.optional(c)) {
			return true
		}
		if(typeof e === "string") {
			e = new RegExp(e)
		}
		return e.test(d)
	});
	a.validator.addMethod("phoneNL", function(d, c) {
		return this.optional(c) || /^((\+|00(\s|\s?\-\s?)?)31(\s|\s?\-\s?)?(\(0\)[\-\s]?)?|0)[1-9]((\s|\s?\-\s?)?[0-9]){8}$/.test(d)
	});
	a.validator.addMethod("phoneUK", function(c, d) {
		c = c.replace(/\(|\)|\s+|-/g, "");
		return this.optional(d) || c.length > 9 && c.match(/^(?:(?:(?:00\s?|\+)44\s?)|(?:\(?0))(?:\d{2}\)?\s?\d{4}\s?\d{4}|\d{3}\)?\s?\d{3}\s?\d{3,4}|\d{4}\)?\s?(?:\d{5}|\d{3}\s?\d{3})|\d{5}\)?\s?\d{4,5})$/)
	});
	a.validator.addMethod("phoneUS", function(c, d) {
		c = c.replace(/\s+/g, "");
		return this.optional(d) || c.length > 9 && c.match(/^(\+?1-?)?(\([2-9]([02-9]\d|1[02-9])\)|[2-9]([02-9]\d|1[02-9]))-?[2-9]([02-9]\d|1[02-9])-?\d{4}$/)
	});
	a.validator.addMethod("phonesUK", function(c, d) {
		c = c.replace(/\(|\)|\s+|-/g, "");
		return this.optional(d) || c.length > 9 && c.match(/^(?:(?:(?:00\s?|\+)44\s?|0)(?:1\d{8,9}|[23]\d{9}|7(?:[1345789]\d{8}|624\d{6})))$/)
	});
	a.validator.addMethod("postalcodeNL", function(d, c) {
		return this.optional(c) || /^[1-9][0-9]{3}\s?[a-zA-Z]{2}$/.test(d)
	});
	a.validator.addMethod("postcodeUK", function(d, c) {
		return this.optional(c) || /^((([A-PR-UWYZ][0-9])|([A-PR-UWYZ][0-9][0-9])|([A-PR-UWYZ][A-HK-Y][0-9])|([A-PR-UWYZ][A-HK-Y][0-9][0-9])|([A-PR-UWYZ][0-9][A-HJKSTUW])|([A-PR-UWYZ][A-HK-Y][0-9][ABEHMNPRVWXY]))\s?([0-9][ABD-HJLNP-UW-Z]{2})|(GIR)\s?(0AA))$/i.test(d)
	});
	a.validator.addMethod("require_from_group", function(g, f, d) {
		var c = a(d[1], f.form),
			i = c.eq(0),
			e = i.data("valid_req_grp") ? i.data("valid_req_grp") : a.extend({}, this),
			h = c.filter(function() {
				return e.elementValue(this)
			}).length >= d[0];
		i.data("valid_req_grp", e);
		if(!a(f).data("being_validated")) {
			c.data("being_validated", true);
			c.each(function() {
				e.element(this)
			});
			c.data("being_validated", false)
		}
		return h
	});
	a.validator.addMethod("skip_or_fill_minimum", function(g, f, d) {
		var c = a(d[1], f.form),
			j = c.eq(0),
			e = j.data("valid_skip") ? j.data("valid_skip") : a.extend({}, this),
			i = c.filter(function() {
				return e.elementValue(this)
			}).length,
			h = i === 0 || i >= d[0];
		j.data("valid_skip", e);
		if(!a(f).data("being_validated")) {
			c.data("being_validated", true);
			c.each(function() {
				e.element(this)
			});
			c.data("being_validated", false)
		}
		return h
	});
	a.validator.addMethod("strippedminlength", function(d, c, e) {
		return a(d).text().length >= e
	});
	a.validator.addMethod("time", function(d, c) {
		return this.optional(c) || /^([01]\d|2[0-3])(:[0-5]\d){1,2}$/.test(d)
	});
	a.validator.addMethod("time12h", function(d, c) {
		return this.optional(c) || /^((0?[1-9]|1[012])(:[0-5]\d){1,2}(\ ?[AP]M))$/i.test(d)
	});
	a.validator.addMethod("url2", function(d, c) {
		return this.optional(c) || /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)*(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(d)
	}, a.validator.messages.url);
	a.validator.addMethod("vinUS", function(q) {
		if(q.length !== 17) {
			return false
		}
		var g = ["A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"],
			p = [1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 7, 9, 2, 3, 4, 5, 6, 7, 8, 9],
			h = [8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2],
			j = 0,
			k, e, o, l, c, m;
		for(k = 0; k < 17; k++) {
			l = h[k];
			o = q.slice(k, k + 1);
			if(k === 8) {
				m = o
			}
			if(!isNaN(o)) {
				o *= l
			} else {
				for(e = 0; e < g.length; e++) {
					if(o.toUpperCase() === g[e]) {
						o = p[e];
						o *= l;
						if(isNaN(m) && e === 8) {
							m = g[e]
						}
						break
					}
				}
			}
			j += o
		}
		c = j % 11;
		if(c === 10) {
			c = "X"
		}
		if(c === m) {
			return true
		}
		return false
	});
	a.validator.addMethod("zipcodeUS", function(d, c) {
		return this.optional(c) || /^\d{5}-\d{4}$|^\d{5}$/.test(d)
	});
	a.validator.addMethod("ziprange", function(d, c) {
		return this.optional(c) || /^90[2-5]\d\{2\}-\d{4}$/.test(d)
	})
}(jQuery));
/*
 * 常用数据类型校验方法：详情参见：jquery.validate.datatype.js
 */
(function(a) {
	a.each({
		chrnum: /^[a-zA-Z0-9]+$/,
		english: /^[A-Za-z ]+$/,
		chinese: /^[\u4e00-\u9fa5]+$/,
		dateTime: /^\d{4}-\d{1,2}-\d{1,2}\s+([0-1][0-9]|2[0-3])(:{1}[0-5]{1}[0-9]{1}){1}$/,
		dateTimeSecond: /^\d{4}-\d{1,2}-\d{1,2}\s+([0-1][0-9]|2[0-3])(:{1}[0-5]{1}[0-9]{1}){2}$/,
		decimal: /^-?\d+(\.\d{1,2})?$/,
		email2: /^((([A-Za-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([A-Za-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([A-Za-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([A-Za-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([A-Za-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([A-Za-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([A-Za-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([A-Za-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([A-Za-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([A-Za-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/,
		"float": /-?([1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0)/,
		floatNumber: /^(0|[1-9][0-9]*)(\.\d+)?$/,
		fourInteger: /^([1-9][0-9]{0,3}|0)$/,
		idCard: /^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/,
		integer: /^-?[1-9]\d*$/,
		LetterNumberUnderline: /^[a-zA-Z][\w]+$/,
		mobile: /^(?:(?:0\d{2,3}-\d{7,8})|\d{7,8}|1\d{10})$/,
		mobileCN: /^1(?:(?:3\d|4[57]|5[0-35-9]|7[6-8]|8\d)\d{8}|3(?:49|80|90)\d{7}|70[015789]\d{7})$/,
		mobileCMC: /^1(?:(?:3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\d{8}|705\d{7}|3[89]0\d{7})$/,
		mobileCUT: /^1(?:(?:3[0-2]|4[5]|5[56]|7[6]|8[56])\d{8}|70[7-9]\d{7})$/,
		mobileCTC: /^1(?:(?:33|53|77|8[019])\d{8}|70[01]\d{7})$/,
		NegativeInteger: /^-[1-9]\d*$/,
		NegativeFloat: /^-([1-9]\d*\.\d*|0\.\d*[1-9]\d*)$/,
		phone: /^1\d{10}$/,
		pingyin: /^[A-Za-z\s\.\·]+$/,
		positiveNumber: /^(([1-9]+[0-9]*.{1}[0-9]+)|([0].{1}[1-9]+[0-9]*)|([1-9][0-9]*)|([0][.][0-9]+[1-9]*))|0?\.0+|0$/,
		PositiveInteger: /^[1-9]\d*$/,
		positiveIntegerFloat: /^[1-9]\d*|[1-9]\d*\.\d*|0\.\d*[1-9]\d*$/,
		PositiveFloat: /^[1-9]\d*\.\d*|0\.\d*[1-9]\d*$/,
		tel: /^(?:(?:0\d{2,3}-\d{7,8})|\d{7,8})$/,
		ThreeNum: /^[0-2]{3}$/,
		UnNegativeInteger: /^[1-9]\d*|0$/,
		UnPositiveInteger: /^-[1-9]\d*|0$/,
		UnNegativeFloat: /^[1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0$/,
		url2: /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/,
		words: /^[\u0391-\uFFE5\w]+$/,
		xfPositiveInteger: /^((0\.5)|([1-9][0-9]*((\.0)|(\.5))?)|0)$/,
		zhxsPositiveInteger: /^((0\.5)|([1-9][0-9]*((\.0)|(\.5))?))$/,
		zipCode: /^[0-9]\d{5}(?!\d)$/,
		zpxs: /^\d+\*\d+$/,
		sjzhou: /^\+[1-9]\d*(\.0)?$/,
		sjxszhou: /^\+[1-9]\d*(\.0)?$|^((0\.5)|([1-9][0-9]*((\.0)|(\.5))?))$/
	}, function(b, c) {
		a.validator.addMethod(b, function(e, d, f) {
			return this.optional(d) || (c.test(e))
		})
	});
	a.validator.addMethod("mobileASGN", function(d, c, e) {
		var b = true;
		if(a.trim(d).length > 0) {
			b = (regexs.tel.regex.test(d))(regexs.mobileCN.regex.test(d))
		}
		return this.optional(c) || b
	})
}(jQuery));
/*
 * 日期时间类校验方法：详情参见：jquery.validate.datetime.js
 */
(function(a) {
	a.validator.addMethod("afterDate", function(c, b, d) {
		if(a(d).founded() && a(b).founded()) {
			return new String(c).toDate().after(new String(a(d).val()).toDate())
		} else {
			return false
		}
	}, function(c, b) {
		if(a(c).founded() && a(b).founded()) {
			return a.validator.format("{0}\u4e0d\u80fd\u65e9\u4e8e\u6216\u7b49\u4e8e{1}!", [a(b).attr("text"), a(c).attr("text")])
		} else {
			if(!a(c).founded()) {
				return a(c).attr("text") + "\u4e0d\u80fd\u4e3a\u7a7a\uff01"
			} else {
				if(!a(b).founded()) {
					return a(b).attr("text") + "\u4e0d\u80fd\u4e3a\u7a7a\uff01"
				}
			}
		}
	});
	a.validator.addMethod("beforeDate", function(c, b, d) {
		if(a(d).founded() && a(b).founded()) {
			return new String(c).toDate().before(new String(a(d).val()).toDate())
		} else {
			return false
		}
	}, function(c, b) {
		if(a(c).founded() && a(b).founded()) {
			return a.validator.format("{0}\u4e0d\u80fd\u665a\u4e8e\u6216\u7b49\u4e8e{1}!", [a(b).attr("text"), a(c).attr("text")])
		} else {
			if(!a(c).founded()) {
				return a(c).attr("text") + "\u4e0d\u80fd\u4e3a\u7a7a\uff01"
			} else {
				if(!a(b).founded()) {
					return a(b).attr("text") + "\u4e0d\u80fd\u4e3a\u7a7a\uff01"
				}
			}
		}
	});
	a.validator.addMethod("equalsDate", function(c, b, d) {
		if(a(d).founded() && a(b).founded()) {
			return new String(c).toDate().equals(new String(a(d).val()).toDate())
		} else {
			return false
		}
	}, function(c, b) {
		if(a(c).founded() && a(b).founded()) {
			return a.validator.format("{0}\u4e0d\u80fd\u4e0e{1}\u76f8\u540c!", [a(b).attr("text"), a(c).attr("text")])
		} else {
			if(!a(c).founded()) {
				return a(c).attr("text") + "\u4e0d\u80fd\u4e3a\u7a7a\uff01"
			} else {
				if(!a(b).founded()) {
					return a(b).attr("text") + "\u4e0d\u80fd\u4e3a\u7a7a\uff01"
				}
			}
		}
	});
	a.validator.addMethod("afterTime", function(c, b, d) {
		if(a(d).founded() && a(b).founded()) {
			return new String(a(b).val()).toTime().after(new String(a(d).val()).toTime())
		} else {
			return false
		}
	}, function(c, b) {
		if(a(c).founded() && a(b).founded()) {
			return a.validator.format("{0}\u4e0d\u80fd\u65e9\u4e8e\u6216\u7b49\u4e8e{1}!", [a(b).attr("text"), a(c).attr("text")])
		} else {
			if(!a(c).founded()) {
				return a(c).attr("text") + "\u4e0d\u80fd\u4e3a\u7a7a\uff01"
			} else {
				if(!a(b).founded()) {
					return a(b).attr("text") + "\u4e0d\u80fd\u4e3a\u7a7a\uff01"
				}
			}
		}
	});
	a.validator.addMethod("beforeTime", function(c, b, d) {
		if(a(d).founded() && a(b).founded()) {
			return new String(a(b).val()).toTime().before(new String(a(d).val()).toTime())
		} else {
			return false
		}
	}, function(c, b) {
		if(a(c).founded() && a(b).founded()) {
			return a.validator.format("{0}\u4e0d\u80fd\u665a\u4e8e\u6216\u7b49\u4e8e{1}!", [a(b).attr("text"), a(c).attr("text")])
		} else {
			if(!a(c).founded()) {
				return a(c).attr("text") + "\u4e0d\u80fd\u4e3a\u7a7a\uff01"
			} else {
				if(!a(b).founded()) {
					return a(b).attr("text") + "\u4e0d\u80fd\u4e3a\u7a7a\uff01"
				}
			}
		}
	});
	a.validator.addMethod("equalsTime", function(c, b, d) {
		if(a(d).founded() && a(b).founded()) {
			return new String(a(b).val()).toTime().equals(new String(a(d).val()).toTime())
		} else {
			return false
		}
	}, function(c, b) {
		if(a(c).founded() && a(b).founded()) {
			return a.validator.format("{0}\u4e0d\u80fd\u4e0e{1}\u76f8\u540c!", [a(b).attr("text"), a(c).attr("text")])
		} else {
			if(!a(c).founded()) {
				return a(c).attr("text") + "\u4e0d\u80fd\u4e3a\u7a7a\uff01"
			} else {
				if(!a(b).founded()) {
					return a(b).attr("text") + "\u4e0d\u80fd\u4e3a\u7a7a\uff01"
				}
			}
		}
	})
}(jQuery));
/*
 * 身份证校验方法：详情参见：jquery.validate.idcard.js
 */
(function(a) {
	function b(g) {
		g = g.toUpperCase();
		if(!(/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(g))) {
			return false
		}
		var h, n;
		h = g.length;
		if(h == 15) {
			n = new RegExp(/^(\d{6})(\d{2})(\d{2})(\d{2})(\d{3})$/);
			var m = g.match(n);
			var e = new Date("19" + m[2] + "/" + m[3] + "/" + m[4]);
			var d;
			d = (e.getYear() == Number(m[2])) && ((e.getMonth() + 1) == Number(m[3])) && (e.getDate() == Number(m[4]));
			if(!d) {
				return false
			} else {
				var k = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
				var l = new Array("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2");
				var j = 0,
					f;
				g = g.substr(0, 6) + "19" + g.substr(6, g.length - 6);
				for(f = 0; f < 17; f++) {
					j += g.substr(f, 1) * k[f]
				}
				g += l[j % 11];
				return true
			}
		}
		if(h == 18) {
			n = new RegExp(/^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/);
			var m = g.match(n);
			var e = new Date(m[2] + "/" + m[3] + "/" + m[4]);
			var d;
			d = (e.getFullYear() == Number(m[2])) && ((e.getMonth() + 1) == Number(m[3])) && (e.getDate() == Number(m[4]));
			if(!d) {
				return false
			} else {
				var c;
				var k = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
				var l = new Array("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2");
				var j = 0,
					f;
				for(f = 0; f < 17; f++) {
					j += g.substr(f, 1) * k[f]
				}
				c = l[j % 11];
				if(c != g.substr(17, 1)) {
					return false
				}
				return true
			}
		}
		return false
	}
	a.validator.addMethod("IDCard", function(d, c) {
		return this.optional(c) || b(d)
	})
}(jQuery));
/*
 * 通用校验方法：详情参见：jquery.validate.methods.js
 */
(function(a) {
	a.validator.addMethod("stringRangeLength", function(e, c, f) {
		var d = e.length;
		for(var b = 0; b < e.length; b += 1) {
			if(e.charCodeAt(b) > 127) {
				d += 1
			}
		}
		return(d >= f[0] && d <= f[1]) || this.optional(c)
	});
	a.validator.addMethod("stringMinLength", function(e, c, f) {
		var d = e.length;
		for(var b = 0; b < e.length; b++) {
			if(e.charCodeAt(b) > 127) {
				d++
			}
		}
		return(d >= f) || this.optional(c)
	});
	a.validator.addMethod("stringMaxLength", function(e, c, f) {
		var d = e.length;
		for(var b = 0; b < e.length; b++) {
			if(e.charCodeAt(b) > 127) {
				d++
			}
		}
		return(d <= f) || this.optional(c)
	});
	a.validator.addMethod("begin", function(d, b, e) {
		var c = new RegExp("^" + e);
		return(c.test(d))
	});
	a.validator.addMethod("notEqualTo", function(c, b, d) {
		return c != a(d).val()
	});
	a.validator.addMethod("notEqual", function(c, b, d) {
		return c != d
	});
	a.validator.addMethod("gt", function(c, b, d) {
		return c > d
	});
	a.validator.addMethod("fixed", function(e, c, f) {
		f = a.isArray(f) ? f : [].concat(f);
		var d = new RegExp(e.replace("/", "/"), "ig");
		var b = d.test(f.join("/")) || d.test(f.join(",")) || d.test(f.join("|"));
		if(!b) {
			a.each(f || [], function(g, h) {
				b = ((h || "".toLowerCase()) == e.toLowerCase());
				return !b
			})
		}
		return b || this.optional(c)
	});
	a.validator.addMethod("fileRequest", function(d, b, e) {
		var d = a(b).val();
		if(!a.founded(d)) {
			return false
		}
		var c = a(b).data("message");
		if(a.founded(c)) {
			return false
		}
		return true
	})
}(jQuery));
/*
 * 密码强度校验方法：详情参见：jquery.validate.strength.js
 */
(function(e) {
	function a(f) {
		var h = 0;
		for(var g = 0; g < 4; g++) {
			if(f & 1) {
				h++
			}
			f >>>= 1
		}
		return h
	}

	function c(f) {
		if(f >= 48 && f <= 57) {
			return 1
		}
		if(f >= 65 && f <= 90) {
			return 2
		}
		if(f >= 97 && f <= 122) {
			return 4
		} else {
			return 8
		}
	}

	function b(g) {
		if(g.length < 6) {
			return 0
		}
		var f = 0;
		for(i = 0; i < g.length; i++) {
			f |= c(g.charCodeAt(i))
		}
		return a(f)
	}
	var d = "\u5bc6\u7801\u5f3a\u5ea6\u8f83\u5f31!";
	e.validator.addMethod("strength", function(g, f, k) {
		var j = true;
		var h = b(g);
		switch(h) {
			case 0:
				d = "\u5bc6\u7801\u957f\u5ea6\u4e3a6~16\u4f4d\uff0c\u81f3\u5c11\u5305\u542b\u6570\u5b57\u3001\u7279\u6b8a\u7b26\u53f7\u3001\u82f1\u6587\u5b57\u6bcd(\u5927\u3001\u5c0f\u5199)\u4e2d\u7684\u4e24\u7c7b!";
				j = false;
				break;
			case 1:
				d = "\u5bc6\u7801\u5f3a\u5ea6\u8f83\u5f31!";
				j = false;
				break;
			default:
				j = true
		}
		return this.optional(f) || j
	}, function(g, f) {
		return d
	})
}(jQuery));
/*
 * 业务框架校验方法：详情参见：jquery.validate.niutal.js
 */
(function(a) {
	a.validator.addMethod("unique", function(e, b, f) {
		if(this.optional(b)) {
			return "dependency-mismatch"
		}
		if(!a.isArray(f)) {
			throw new Error(" \u53c2\u6570\u5fc5\u987b\u662f\u6570\u7ec4! ")
		}
		if(a.trim(e).length == 0) {
			return false
		}
		var c = {
			filed_name: a(b).data("namemapper") || a(b).attr("name"),
			filed_value: e,
			old_filed_value: "",
			table: f[0] || ""
		};
		if(f.length >= 2) {
			c.old_filed_value = f[1] || ""
		}
		var d = false;
		a.ajax({
			type: "POST",
			url: _path + "/xtgl/validate_cxUnique.html",
			async: false,
			data: c,
			success: function(g) {
				if(parseInt(g || "0") == 1) {
					d = true
				} else {
					d = false
				}
			}
		});
		return d
	}, function(c, b) {
		if(a.trim(a(b).val()).length == 0) {
			return "\u4e0d\u80fd\u4e3a\u7a7a!"
		} else {
			if(c.length == 3) {
				return c[2]
			} else {
				return "\u5df2\u7ecf\u5b58\u5728\uff1b\u8fdd\u53cd\u552f\u4e00\u7ea6\u675f!"
			}
		}
	});
	a.validator.addMethod("multiUnique", function(f, c, g) {
		if(this.optional(c)) {
			return "dependency-mismatch"
		}
		if(!a.isArray(g)) {
			throw new Error(" \u53c2\u6570\u5fc5\u987b\u662f\u6570\u7ec4! ")
		}
		if(g.length <= 1) {
			throw new Error(" \u6570\u7ec4\u81f3\u5c11\u4e24\u4e2a\u53c2\u6570! ")
		}
		var b = true;
		if(a.isArray(g[1])) {
			a.each(g[1], function(j, h) {
				if(a.trim(a(h).val()).length < 1) {
					b = false;
					return false
				}
			})
		} else {
			if(a.trim(a(g[1]).val()).length < 1) {
				b = false
			}
		}
		if(!b) {
			return true
		}
		var d = {
			table: g[0] || ""
		};
		if(a.isArray(g[1])) {
			a.each(g[1], function(j, h) {
				if(a(h).founded()) {
					d["filed_list[" + j + "].key"] = a(h).data("namemapper") || a(h).attr("name");
					d["filed_list[" + j + "].value"] = a(h).val()
				}
			})
		} else {
			if(a(g[1]).founded()) {
				d["filed_list[0].key"] = a(g[1]).data("namemapper") || a(g[1]).attr("name");
				d["filed_list[0].value"] = a(g[1]).val()
			}
		}
		if(g.length == 4) {
			if(a.isArray(g[1]) && a.isArray(g[2])) {
				if(g[1].length != g[2].length) {
					throw new Error(" \u9a8c\u8bc1\u5143\u7d20\u4e2a\u6570\u4e0e\u539f\u59cb\u503c\u4e2a\u6570\u4e0d\u76f8\u540c! ")
				}
				a.each(g[1], function(j, h) {
					if(a(h).founded()) {
						d["old_filed_list[" + j + "].key"] = a(h).data("namemapper") || a(h).attr("name");
						d["old_filed_list[" + j + "].value"] = g[2][j]
					}
				})
			} else {
				if(a(g[2]).founded()) {
					d["old_filed_list[0].key"] = a(g[2]).data("namemapper") || a(g[2]).attr("name");
					d["old_filed_list[0].value"] = g[2][0]
				}
			}
		}
		var e = false;
		a.ajax({
			type: "POST",
			url: _path + "/xtgl/validate_cxUnique.html",
			async: false,
			data: d,
			success: function(h) {
				if(parseInt(h || "0") == 1) {
					e = true
				} else {
					e = false
				}
			}
		});
		return e
	}, function(d, b) {
		if(a.isArray(d)) {
			var c = "";
			if(d.length == 3) {
				c = d[2]
			} else {
				if(d.length == 4) {
					c = d[3]
				}
			}
			return c
		} else {
			return "\u5df2\u7ecf\u5b58\u5728\uff1b\u8fdd\u53cd\u552f\u4e00\u7ea6\u675f!"
		}
	});
	a.validator.addMethod("jsConflict", function(l, i, e) {
		if(!a.isArray(e)) {
			throw new Error(" \u53c2\u6570\u5fc5\u987b\u662f\u6570\u7ec4! ")
		}
		var c = a(e[0]).val();
		var b = a(e[1]).val();
		var g = a(e[2]).val();
		var d = a(e[3]).val();
		var f = [];
		a(e[4]).find("td.ui-selected").each(function(n, o) {
			f.push(a(o).data("zc") || a(o).text())
		});
		f = f.sort(function(o, n) {
			return o - n
		});
		var h = 0;
		a.each(f, function(n, o) {
			h += Math.pow(2, o - 1)
		});
		var j = a.parseJSON(a(e[5]).data("skdjd") || "[]");
		var k = null;
		a.each(j, function(n, p) {
			var o = p.jc.split("-");
			for(var n = parseInt(o[0]); n <= parseInt(o[1]); n++) {
				k += Math.pow(2, n - 1)
			}
		});
		var m = false;
		a.ajax({
			type: "POST",
			url: _path + "/jxjhgl/commonPk_cxJsctValidate.html",
			async: false,
			data: {
				xnm: c || "",
				xqm: b || "",
				jgh_id: d || "",
				xqj: xqj || "",
				jc: k || "",
				zcd: h || "",
				jxb_id: g || ""
			},
			success: function(n) {
				if(a.founded(n)) {
					a(i).data("message", n);
					m = false
				} else {
					m = true
				}
			}
		});
		return m
	}, function(d, b) {
		var c = a(b).data("message");
		a(b).data("message", "");
		return "\u4e0e" + c + "\u51b2\u7a81!"
	});
	a.validator.addMethod("validEmail", function(d, c, e) {
		if(this.optional(c)) {
			return "dependency-mismatch"
		}
		var b = false;
		a.ajax({
			type: "POST",
			url: _path + "/xtgl/validate_cxValidEmail.html",
			async: false,
			data: {
				yjzh: d
			},
			success: function(f) {
				if("1" == f) {
					b = true
				}
			}
		});
		return b
	}, a.validator.format("\u90ae\u7bb1\u7c7b\u578b\u4e0d\u5728\u89c4\u5b9a\u7c7b\u578b\u5185"))
}(jQuery));