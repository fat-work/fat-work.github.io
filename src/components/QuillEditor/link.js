import Quill from "quill";
const InlineEmbed = Quill.import("blots/inline");

// 自定义超链接
class Link extends InlineEmbed {
  static blotName = "link";
  static tagName = "A";
  static SANITIZED_URL = "about:blank";
  static PROTOCOL_WHITELIST = ["http", "https", "mailto", "tel", "sms"];

  static create(value) {
    let node = undefined;
    if (value && !value.href) {
      // 自身Link Blot
      node = super.create(value);
      node.setAttribute("href", this.sanitize(value));
      node.setAttribute("rel", "noopener noreferrer");
      node.setAttribute("target", "_blank");
    } else {
      // 自定义Link Blot
      node = super.create(value.href);
      node.setAttribute("href", value.href); // 左键点击即下载
      node.setAttribute("download", value.innerText); // 左键点击即下载
      node.innerText = value.innerText;
    }
    return node;
  }

  static formats(domNode) {
    return domNode.getAttribute("href");
  }

  static sanitize(url) {
    return sanitize(url, this.PROTOCOL_WHITELIST) ? url : this.SANITIZED_URL;
  }

  format(name, value) {
    if (name !== this.statics.blotName || !value) {
      super.format(name, value);
    } else {
      // @ts-expect-error
      this.domNode.setAttribute("href", this.constructor.sanitize(value));
    }
  }
}

function sanitize(url, protocols) {
  const anchor = document.createElement("a");
  anchor.href = url;
  const protocol = anchor.href.slice(0, anchor.href.indexOf(":"));
  return protocols.indexOf(protocol) > -1;
}

export default Link;
