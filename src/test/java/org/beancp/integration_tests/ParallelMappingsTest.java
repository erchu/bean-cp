/*
 * bean-cp
 * Copyright (c) 2014, Rafal Chojnacki, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package org.beancp.integration_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

import org.beancp.Mapper;
import org.beancp.MapperBuilder;
import org.beancp.commons.CollectionConverters;
import org.beancp.commons.NameBasedMapConvention;
import org.beancp.commons.NumberConverters;
import org.junit.Test;

public class ParallelMappingsTest {
    
    private static final int NUMBER_OF_THREADS = 10;

    private static final int TEST_DURATION_SECONDS = 20;
    
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

    public static class ReferenceHolder<T> {

        private final T reference;

        public ReferenceHolder(final T reference) {
            this.reference = reference;
        }

        public T getReference() {
            return reference;
        }
    }

    public static class PointHolder {

        private final Point reference;

        public PointHolder(final Point reference) {
            this.reference = reference;
        }

        public Point getReference() {
            return reference;
        }
    }

    public static class Point {     // Test NameBasedConvention, including
                                    // failIfNotAllDestinationMembersMapped option

        private int x;  // Test Map.bind()

        public int y;  // Test Map.bind()

        private AuthorInfo author;
        
        private AuditLog audit;

        private PointExtension extension;

        private Point directReference;

        private ReferenceHolder<Point> indirectReferenceToMap;

        private PointHolder indirectReferenceToConvert;

        public Point getDirectReference() {
            return directReference;
        }

        public void setDirectReference(Point directReference) {
            this.directReference = directReference;
        }

        public ReferenceHolder<Point> getIndirectReferenceToMap() {
            return indirectReferenceToMap;
        }

        public void setIndirectReferenceToMap(ReferenceHolder<Point> indirectReferenceToMap) {
            this.indirectReferenceToMap = indirectReferenceToMap;
        }

        public PointHolder getIndirectReferenceToConvert() {
            return indirectReferenceToConvert;
        }

        public void setIndirectReferenceToConvert(PointHolder indirectReferenceToConvert) {
            this.indirectReferenceToConvert = indirectReferenceToConvert;
        }

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

        private PointInfo directReference;

        private ReferenceHolder<PointInfo> indirectReferenceToMap;

        private PointInfo indirectReferenceToConvert;

        public PointInfo getDirectReference() {
            return directReference;
        }

        public void setDirectReference(PointInfo directReference) {
            this.directReference = directReference;
        }

        public ReferenceHolder<PointInfo> getIndirectReferenceToMap() {
            return indirectReferenceToMap;
        }

        public void setIndirectReferenceToMap(ReferenceHolder<PointInfo> indirectReferenceToMap) {
            this.indirectReferenceToMap = indirectReferenceToMap;
        }

        public PointInfo getIndirectReferenceToConvert() {
            return indirectReferenceToConvert;
        }

        public void setIndirectReferenceToConvert(PointInfo indirectReferenceToConvert) {
            this.indirectReferenceToConvert = indirectReferenceToConvert;
        }

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

    private class MappingThread implements Runnable {

        private final Mapper mapper;

        private final Random random;

        public MappingThread(final Mapper mapper) {
            this.mapper = mapper;
            this.random = new Random();
        }

        @Override
        public void run() {
            LocalDateTime start = LocalDateTime.now();

            do {
                executeMapping();
            } while (testTimeElapsed(start) == false);
        }

        private boolean testTimeElapsed(final LocalDateTime start) {
            Duration testDuration = Duration.between(start, LocalDateTime.now());

            return (testDuration.getSeconds() > TEST_DURATION_SECONDS);
        }

        private void executeMapping() {
            PointExtension pointExtension = new PointExtension();
            pointExtension.setZ((long)random.nextInt());
            
            int otherDimensionNumber = random.nextInt(10);
            
            for (int i = 0; i < otherDimensionNumber; i++) {
                pointExtension.getOtherDimensions().add(i);
            }

            Point source = new Point();
            source.setX(random.nextInt());
            source.y = random.nextInt();
            source.setExtension(pointExtension);
            source.setAuthor(AuthorInfo.getFromName("U" + random.nextInt()));
            source.setDirectReference(source);
            source.setIndirectReferenceToConvert(new PointHolder(source));
            source.setIndirectReferenceToMap(new ReferenceHolder<>(source));
            
            AuditLog auditLog = new AuditLog();
            auditLog.setCreatedOn(new Date());
            auditLog.setUpdatedOn(new Date());
            
            source.setAudit(auditLog);

            PointInfo result = mapper.map(source, PointInfo.class);

            assertEquals(source.getX() + source.y, result.getMetric());
            assertEquals(source.getExtension().getZ().intValue(), result.getExtensionZ());
            assertEquals(source.getAuthor().getName(), result.getAuthor());
            
            assertEquals(
                    source.getExtension().getOtherDimensions().size(),
                    result.getExtensionOtherDimensions().length);
            
            for (int i = 0; i < result.getExtensionOtherDimensions().length; i++) {
                assertEquals(i, result.getExtensionOtherDimensions()[i]);
            }
            
            assertEquals(source.getAudit().getCreatedOn(), result.getAudit().getCreatedOn());
            assertEquals(source.getAudit().getUpdatedOn(), result.getAudit().getUpdatedOn());
            
            // recursive references
            assertEquals(result, result.getDirectReference());
            assertEquals(result, result.getIndirectReferenceToConvert());
            assertEquals(result, result.getIndirectReferenceToMap().getReference());
        }
    }

    private static class ThreadStatus {

        private volatile boolean success;

        public ThreadStatus(final boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess() {
            this.success = true;
        }

        public void setFailure() {
            this.success = false;
        }
    }

    @Test
    public void mapper_should_be_able_to_map_objects_in_parallel_threads()
            throws InterruptedException {
        // GIVEN
        Mapper mapper = new MapperBuilder()
                .addMapAnyByConvention(NameBasedMapConvention.get())
                .addConverter(PointHolder.class, PointInfo.class, (mapperRef, source)
                        -> mapperRef.map(source.getReference(), PointInfo.class))
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
                        .bind(() -> source.getX() + source.y, destination::setMetric)
                        .mapInner(
                                () -> source.getIndirectReferenceToMap() != null ? source.getIndirectReferenceToMap().getReference() : null,
                                (PointInfo value) -> {
                                    destination.setIndirectReferenceToMap(new ReferenceHolder<>(value));
                                },
                                () -> destination.getIndirectReferenceToMap() != null ? destination.getIndirectReferenceToMap().getReference() : null,
                                PointInfo.class))
                .buildMapper();

        // WHEN
        Thread[] threads = new Thread[NUMBER_OF_THREADS];
        ThreadStatus status = new ThreadStatus(true);

        for (int i = 0 ; i < threads.length ; i++) {
            Thread iThread = new Thread(new MappingThread(mapper), "Test #" + i);

            iThread.setUncaughtExceptionHandler((Thread t, Throwable e) -> {
                status.setFailure();

                System.err.println(e);
            });

            threads[i] = iThread;

            iThread.start();
        }

        for (Thread i : threads) {
            i.join();
        }

        // THEN
        assertTrue("Something went wrong...", status.isSuccess());
    }
}
