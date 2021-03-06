package org.unidal.webres.server.css;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.annotation.ContextPath;
import org.unidal.webres.resource.api.ICss;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class SimpleWarCssUrlBuilder implements IResourceUrlBuilder<ICss>, IResourceRegisterable<SimpleWarCssUrlBuilder> {
   private String m_contextPath;

   private String m_servletPath;

   public SimpleWarCssUrlBuilder(String servletPath) {
      m_servletPath = servletPath;

      if (servletPath != null) {
         if (!servletPath.startsWith("/") && servletPath.endsWith("/")) {
            throw new RuntimeException("servletPath should be null or starting with '/' but not ending with '/'.");
         }
      }
   }

   @Override
   public String build(IResourceContext ctx, ICss Css) {
      StringBuilder sb = new StringBuilder(128);

      if (m_contextPath != null) {
         sb.append(m_contextPath);
      }

      if (m_servletPath != null) {
         sb.append(m_servletPath);
      }

      IResourceUrn urn = Css.getMeta().getUrn();

      sb.append('/').append(urn.getResourceTypeName());
      sb.append('/').append(urn.getNamespace());
      sb.append('/').append(getWarName(urn));

      if (ctx.getPermutation() != null && !ctx.isFallbackPermutation()) {
         sb.append('/').append(ctx.getPermutation().toExternal());
      }

      sb.append(urn.getPathInfo());

      return sb.toString();
   }

   @Override
   public SimpleWarCssUrlBuilder getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Css.War;
   }

   @Override
   public Class<? super SimpleWarCssUrlBuilder> getRegisterType() {
      return IResourceUrlBuilder.class;
   }

   protected String getWarName(IResourceUrn urn) {
      String path = urn.getResourceId();
      int pos = path.indexOf('/', 1);
      String warName = path.substring(1, pos);
      return warName;
   }

   @ContextPath
   public void setContextPath(String contextPath) {
      m_contextPath = contextPath;
   }
}
