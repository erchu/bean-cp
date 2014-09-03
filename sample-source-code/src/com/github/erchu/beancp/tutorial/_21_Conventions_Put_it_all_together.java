package com.github.erchu.beancp.tutorial;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.github.erchu.beancp.Mapper;
import com.github.erchu.beancp.MapperBuilder;
import com.github.erchu.beancp.commons.CollectionConverters;
import com.github.erchu.beancp.commons.NameBasedMapConvention;
import com.github.erchu.beancp.commons.NumberConverters;

public class _21_Conventions_Put_it_all_together {

    public static class AuditLog {  // Test addMapAnyByConvention

        private Date createdOn;

        private Date updatedOn;

        public Date getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(Date createdOn) {
            this.createdOn = createdOn;
        }

        public Date getUpdatedOn() {
            return updatedOn;
        }

        public void setUpdatedOn(Date updatedOn) {
            this.updatedOn = updatedOn;
        }
    }

    public static class AuthorInfo {    // Test converter (to String)

        private String name;

        public static AuthorInfo getFromName(final String name) {
            AuthorInfo result = new AuthorInfo();
            result.name = name;

            return result;
        }

        public String getName() {
            return name;
        }
    }

    public static class PointExtension {    // Test flattening

        private Long z;     // Test NumberConverter

        private final Collection<Integer> otherDimensions;  // Test CollectionConverters

        public PointExtension() {
            this.otherDimensions = new LinkedList<>();
        }

        public Collection<Integer> getOtherDimensions() {
            return otherDimensions;
        }

        public Long getZ() {
            return z;
        }

        public void setZ(Long z) {
            this.z = z;
        }
    }

    public static class Point {     // Test NameBasedConvention, including
        							// failIfNotAllDestinationMembersMapped option

        private int x;  // Test DeclarativeMap.bind()

        public int y;  // Test DeclarativeMap.bind()

        private AuthorInfo author;

        private AuditLog audit;

        private PointExtension extension;

        public AuditLog getAudit() {
            return audit;
        }

        public void setAudit(AuditLog audit) {
            this.audit = audit;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public PointExtension getExtension() {
            return extension;
        }

        public void setExtension(PointExtension extension) {
            this.extension = extension;
        }

        public AuthorInfo getAuthor() {
            return author;
        }

        public void setAuthor(AuthorInfo author) {
            this.author = author;
        }
    }

    public static class PointInfo {

        private int metric;

        private int extensionZ;

        private int[] extensionOtherDimensions;

        private String author;

        private AuditLogInfo audit;

        public AuditLogInfo getAudit() {
            return audit;
        }

        public void setAudit(AuditLogInfo audit) {
            this.audit = audit;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getMetric() {
            return metric;
        }

        public void setMetric(int metric) {
            this.metric = metric;
        }

        public int getExtensionZ() {
            return extensionZ;
        }

        public void setExtensionZ(int extensionZ) {
            this.extensionZ = extensionZ;
        }

        public int[] getExtensionOtherDimensions() {
            return extensionOtherDimensions;
        }

        public void setExtensionOtherDimensions(int[] extensionOtherDimensions) {
            this.extensionOtherDimensions = extensionOtherDimensions;
        }
    }

    public static class AuditLogInfo {

        private Date createdOn;

        private Date updatedOn;

        public Date getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(Date createdOn) {
            this.createdOn = createdOn;
        }

        public Date getUpdatedOn() {
            return updatedOn;
        }

        public void setUpdatedOn(Date updatedOn) {
            this.updatedOn = updatedOn;
        }
    }

    private final Random random = new Random();

	@Test
    @SuppressWarnings("unchecked")
    public void mapper_should_map_objects_in_parallel_threads()
            throws InterruptedException, ExecutionException {
        // Configure mapper
        Mapper mapper = new MapperBuilder()
                .addMapAnyByConvention(NameBasedMapConvention.get())
                .addConverter(AuthorInfo.class, String.class, source -> {
                    return source.getName();
                })
                .addConverter(CollectionConverters.getCollectionToArray(int.class))
                .addConverter(NumberConverters.get())
                .addMap(
                        Point.class,
                        PointInfo.class,
                        (config, source, destination) -> config
                        .useConvention(NameBasedMapConvention.get()
                                .enableFlattening()
                                .excludeDestinationMembers("metric")
                                .failIfNotAllDestinationMembersMapped())
                        .bind(() -> source.getX() + source.y, destination::setMetric))
                .buildMapper();
        
        // Sample data

        PointExtension pointExtension = new PointExtension();
        pointExtension.setZ((long) random.nextInt());

        int otherDimensionNumber = random.nextInt(10);

        for (int i = 0 ; i < otherDimensionNumber ; i++) {
            pointExtension.getOtherDimensions().add(i);
        }

        Point source = new Point();
        source.setX(random.nextInt());
        source.y = random.nextInt();
        source.setExtension(pointExtension);
        source.setAuthor(AuthorInfo.getFromName("U" + random.nextInt()));

        AuditLog auditLog = new AuditLog();
        auditLog.setCreatedOn(new Date());
        auditLog.setUpdatedOn(new Date());

        source.setAudit(auditLog);

        // Map
        @SuppressWarnings("unused")
		PointInfo result = mapper.map(source, PointInfo.class);
    }
}
