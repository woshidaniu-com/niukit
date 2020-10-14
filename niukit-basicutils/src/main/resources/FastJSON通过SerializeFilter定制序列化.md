原文地址:https://github.com/alibaba/fastjson/wiki/SerializeFilter
简介

SerializeFilter是通过编程扩展的方式定制序列化。fastjson支持6种SerializeFilter，用于不同场景的定制序列化。

    PropertyPreFilter 根据PropertyName判断是否序列化
    PropertyFilter 根据PropertyName和PropertyValue来判断是否序列化
    NameFilter 修改Key，如果需要修改Key,process返回值则可
    ValueFilter 修改Value
    BeforeFilter 序列化时在最前添加内容
    AfterFilter 序列化时在最后添加内容

PropertyFilter 根据PropertyName和PropertyValue来判断是否序列化

  public interface PropertyFilter extends SerializeFilter {
      boolean apply(Object object, String propertyName, Object propertyValue);
  }

可以通过扩展实现根据object或者属性名称或者属性值进行判断是否需要序列化。例如：

PropertyFilter filter = new PropertyFilter() {

    public boolean apply(Object source, String name, Object value) {
        if ("id".equals(name)) {
            int id = ((Integer) value).intValue();
            return id >= 100;
        }
        return false;
    }
};

JSON.toJSONString(obj, filter); // 序列化的时候传入filter

PropertyPreFilter 根据PropertyName判断是否序列化

和PropertyFilter不同只根据object和name进行判断，在调用getter之前，这样避免了getter调用可能存在的异常。

 public interface PropertyPreFilter extends SerializeFilter {
      boolean apply(JSONSerializer serializer, Object object, String name);
  }

NameFilter 序列化时修改Key

如果需要修改Key,process返回值则可

  public interface NameFilter extends SerializeFilter {
      String process(Object object, String propertyName, Object propertyValue);
  }

ValueFilter 序列化是修改Value

  public interface ValueFilter extends SerializeFilter {
      Object process(Object object, String propertyName, Object propertyValue);
  }

BeforeFilter 序列化时在最前添加内容

  public abstract class BeforeFilter implements SerializeFilter {
      protected final void writeKeyValue(String key, Object value) { ... }
      // 需要实现的抽象方法，在实现中调用writeKeyValue添加内容
      public abstract void writeBefore(Object object);
  }

AfterFilter 序列化时在最前添加内容

  public abstract class AfterFilter implements SerializeFilter {
      protected final void writeKeyValue(String key, Object value) { ... }
      // 需要实现的抽象方法，在实现中调用writeKeyValue添加内容
      public abstract void writeAfter(Object object);
  }
