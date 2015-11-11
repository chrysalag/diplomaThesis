/**
 * Parameter for Direct Association Matrix.
 * It determines how much is the ranking vector returned from
 * {@code HIRItemScorer} affected by the coratings.
 */

@Documented
@DefaultDouble(0.6)
@Parameter(Double.class)
@Qualifier
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DirectAssociationParameter {
}
